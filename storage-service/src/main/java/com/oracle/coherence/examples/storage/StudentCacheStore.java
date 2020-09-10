package com.oracle.coherence.examples.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import javax.sql.DataSource;

import com.oracle.coherence.common.base.Exceptions;
import com.oracle.coherence.examples.domain.Address;
import com.oracle.coherence.examples.domain.Student;
import com.oracle.coherence.examples.domain.StudentId;

import com.tangosol.net.cache.CacheStore;

import oracle.jdbc.pool.OracleDataSource;

/**
 * @author Jonathan Knight  2020.09.09
 */
public class StudentCacheStore
        implements CacheStore<StudentId, Student> {

    public static final String PROP_DB_HOST = "oracle.db.host";
    public static final String PROP_DB_PORT = "oracle.db.port";
    public static final String PROP_DB_SID = "oracle.db.sid";
    public static final String PROP_DB_USER = "oracle.db.user";
    public static final String PROP_DB_PWD = "oracle.db.pwd";

    private final DataSource dataSource;

    private final String dbUser;

    private final String dbPass;

    public StudentCacheStore() throws SQLException {
        this(createOracleDatasource(), System.getProperty(PROP_DB_USER), System.getProperty(PROP_DB_PWD));
    }

    // ----- constructors ---------------------------------------------------

    StudentCacheStore(DataSource dataSource, String dbUser, String dbPass) {
        this.dataSource = Objects.requireNonNull(dataSource);

        if (dbUser == null || dbUser.isBlank()) {
            throw new IllegalArgumentException("The database user cannot be null or blank");
        }
        if (dbPass == null || dbPass.isBlank()) {
            throw new IllegalArgumentException("The database password cannot be null or blank");
        }

        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    // ----- CacheStore implementation --------------------------------------

    @Override
    public Student load(StudentId studentId) {
        try {
            try(Connection connection = createConnection()) {
                return loadStudent(studentId, connection);
            }
        }
        catch (SQLException e) {
            throw Exceptions.ensureRuntimeException(e);
        }
    }

    @Override
    public void store(StudentId studentId, Student student) {
        try {
            try(Connection connection = createConnection()) {
                try {
                    storeAddress(studentId, student, connection);
                    storeStudent(studentId, student, connection);
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                    throw Exceptions.ensureRuntimeException(e);
                }
            }
        }
        catch (SQLException e) {
            throw Exceptions.ensureRuntimeException(e);
        }
    }

    @Override
    public void erase(StudentId studentId) {
        try {
            try(Connection connection = createConnection()) {
                String deleteStudentSQL = "delete from student where roll = ?";
                String deleteAddressSQL = "delete from address where roll = ?";

                try (PreparedStatement stmtStudent = connection.prepareStatement(deleteStudentSQL);
                     PreparedStatement stmtAddress = connection.prepareStatement(deleteAddressSQL)) {

                    stmtStudent.setString(1, studentId.getRollNumber());
                    stmtStudent.execute();
                    stmtAddress.setString(1, studentId.getRollNumber());
                    stmtAddress.execute();
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                }
            }
        }
        catch (SQLException e) {
            throw Exceptions.ensureRuntimeException(e);
        }
    }

    // ----- helper methods -------------------------------------------------

    private Connection createConnection() throws SQLException {
        Connection connection = dataSource.getConnection(dbUser, dbPass);
        connection.setAutoCommit(false);
        return connection;
    }

    /**
     * Load a Student from the database.
     *
     * @param studentId   the student id
     * @param connection  the database connection to use
     *
     * @return the loaded Student or {@code null} if there was no Student with the Id
     *
     * @throws SQLException if an error occurs executing the database statements
     */
    private Student loadStudent(StudentId studentId, Connection connection) throws SQLException {
        String sql = "select first_name, last_name, class_name from student where roll = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentId.getRollNumber());
            statement.execute();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (!resultSet.next()) {
                    // student not in DB
                    return null;
                }

                String firstName = resultSet.getString(1);
                String lastName = resultSet.getString(2);
                String className = resultSet.getString(3);
                Address address = loadAddress(studentId, connection);

                return new Student(studentId.getRollNumber(), firstName, lastName, className, address);
            }
        }
    }

    /**
     * Load an Address from the database.
     *
     * @param studentId   the student id for the address to load
     * @param connection  the database connection to use
     *
     * @return the loaded Address or {@code null} if there was no address for the student
     *
     * @throws SQLException if an error occurs executing the database statements
     */
    private Address loadAddress(StudentId studentId, Connection connection) throws SQLException {
        String sql = "select line_one, line_two, city, postal_code, country from address where roll = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentId.getRollNumber());
            statement.execute();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (!resultSet.next()) {
                    // address not in DB
                    return null;
                }

                Address address = new Address();
                address.setLineOne(resultSet.getString(1));
                address.setLineTwo(resultSet.getString(2));
                address.setCity(resultSet.getString(3));
                address.setPostalCode(resultSet.getString(4));
                address.setCountry(resultSet.getString(5));

                return address;
            }
        }
    }

    /**
     * Store a Student in the database.
     *
     * @param studentId   the Student Id
     * @param student     the Student to store
     * @param connection  the database connection to use
     *
     * @throws SQLException if an error occurs executing the database statements
     */
    private void storeStudent(StudentId studentId, Student student, Connection connection) throws SQLException {
        Address address = student.getAddress();
        if (address == null) {
            // no address so do a delete to ensure that the address is removed
            return;
        }

        // We do a merge statement so that it does not matter whether the address exists or not
        String mergeSQL = "merge into student s using ( "
                + "  select ? as roll, "
                + "         ? as first_name, "
                + "         ? as last_name, "
                + "         ? as class_name "
                + "   from dual) data on (s.roll = data.roll) "
                + "when matched then "
                + "update set s.first_name = data.first_name, "
                + "           s.last_name  = data.last_name, "
                + "           s.class_name = data.class_name "
                + "when not matched then "
                + "insert (roll, first_name, last_name, class_name, address_roll) "
                + "    values (data.roll, data.first_name, data.last_name, data.class_name, data.roll)";

        try (PreparedStatement statement = connection.prepareStatement(mergeSQL)) {
            statement.setString(1, studentId.getRollNumber());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            statement.setString(4, student.getClassName());
            statement.execute();
        }
    }

    /**
     * Store an Address in the database.
     *
     * @param studentId   the Student Id for the address to store
     * @param student     the Student containing the address to store
     * @param connection  the database connection to use
     *
     * @throws SQLException if an error occurs executing the database statements
     */
    private void storeAddress(StudentId studentId, Student student, Connection connection) throws SQLException {
        Address address = student.getAddress();
        if (address == null) {
            // no address so do a delete to ensure that the address is removed
            return;
        }

        // We do a merge statement so that it does not matter whether the address exists or not
        String mergeSQL = "merge into address a using ( "
                + "  select ? as roll, "
                + "         ? as line_one, "
                + "         ? as line_two, "
                + "         ? as city, "
                + "         ? as postal_code, "
                + "         ? as country "
                + "   from dual) data on (a.roll = data.roll) "
                + "when matched then "
                + "update set a.line_one    = data.line_one, "
                + "           a.line_two    = data.line_two, "
                + "           a.city        = data.city, "
                + "           a.postal_code = data.postal_code, "
                + "           a.country    = data.country "
                + "when not matched then "
                + "insert (roll, line_one, line_two, city, postal_code, country) "
                + "    values (data.roll, data.line_one, data.line_two, data.city, data.postal_code, data.country)";

        try (PreparedStatement statement = connection.prepareStatement(mergeSQL)) {
            statement.setString(1, studentId.getRollNumber());
            statement.setString(2, address.getLineOne());
            statement.setString(3, address.getLineTwo());
            statement.setString(4, address.getCity());
            statement.setString(5, address.getPostalCode());
            statement.setString(6, address.getCountry());
            statement.execute();
        }
    }

    static DataSource createOracleDatasource() throws SQLException {

        String host = System.getProperty(PROP_DB_HOST);
        String port = System.getProperty(PROP_DB_PORT);
        String sid = System.getProperty(PROP_DB_SID);
        String url = String.format("jdbc:oracle:thin:@%s:%s/%s", host, port, sid);
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setURL(url);
        return dataSource;
    }
}
