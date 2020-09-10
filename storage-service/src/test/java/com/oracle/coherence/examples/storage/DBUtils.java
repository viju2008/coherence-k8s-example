package com.oracle.coherence.examples.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.oracle.coherence.examples.domain.Address;
import com.oracle.coherence.examples.domain.Student;
import com.oracle.coherence.examples.domain.StudentId;

import oracle.jdbc.pool.OracleDataSource;

import static com.oracle.coherence.examples.storage.StudentCacheStore.PROP_DB_HOST;
import static com.oracle.coherence.examples.storage.StudentCacheStore.PROP_DB_PORT;
import static com.oracle.coherence.examples.storage.StudentCacheStore.PROP_DB_SID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Jonathan Knight  2020.09.09
 */
public class DBUtils {
    private final OracleDataSource dataSource;
    private final String testUser;
    private final String testPassword;

    public DBUtils() throws SQLException {
        String host = System.getProperty(PROP_DB_HOST);
        String port = System.getProperty(PROP_DB_PORT);
        String sid = System.getProperty(PROP_DB_SID);
        String url = String.format("jdbc:oracle:thin:@%s:%s/%s", host, port, sid);
        dataSource = new OracleDataSource();
        dataSource.setURL(url);
        testUser = System.getProperty(StudentCacheStore.PROP_DB_USER);
        testPassword = System.getProperty(StudentCacheStore.PROP_DB_PWD);
    }

    public OracleDataSource getDataSource() {
        return dataSource;
    }

    public String getTestUser() {
        return testUser;
    }

    public String getTestPassword() {
        return testPassword;
    }

    void assertStudentInDB(StudentId id, Student student) throws SQLException {
        try (Connection conn = dataSource.getConnection(testUser, testPassword)) {
            String studentSql = String.format("select roll, firstName, lastName, class from students where roll = '%s'", id.getRollNumber());
            try (PreparedStatement statement = conn.prepareStatement(studentSql)) {
                statement.execute();
                try (ResultSet resultSet = statement.getResultSet()) {
                    assertThat("Did not find Student " + id + " in DB", resultSet.next(), is(true));
                    assertThat(resultSet.getString(1), is(student.getRollNumber()));
                    assertThat(resultSet.getString(2), is(student.getFirstName()));
                    assertThat(resultSet.getString(3), is(student.getLastName()));
                    assertThat(resultSet.getString(4), is(student.getClassName()));
                }
            }

            String addressSql = String.format("select lineOne, lineTwo, city, postalCode, country from addresses where roll = '%s'", id.getRollNumber());
            try (PreparedStatement statement = conn.prepareStatement(addressSql)) {
                statement.execute();
                try (ResultSet resultSet = statement.getResultSet()) {
                    Address address = student.getAddress();
                    if (address == null) {
                        // should not find address
                        assertThat("Should not have found Address " + id + " in DB", resultSet.next(), is(false));
                    } else {
                        assertThat("Did not find Address " + id + " in DB", resultSet.next(), is(true));
                        assertThat(resultSet.getString(1), is(address.getLineOne()));
                        assertThat(resultSet.getString(2), is(address.getLineTwo()));
                        assertThat(resultSet.getString(3), is(address.getCity()));
                        assertThat(resultSet.getString(4), is(address.getPostalCode()));
                        assertThat(resultSet.getString(5), is(address.getCountry()));
                    }
                }
            }
        }
    }

    void assertStudentNotInDB(StudentId id) throws SQLException {
        try (Connection conn = dataSource.getConnection(testUser, testPassword)) {
            String studentSql = String.format("select * from students where roll = '%s'", id.getRollNumber());
            try (PreparedStatement statement = conn.prepareStatement(studentSql)) {
                statement.execute();
                try (ResultSet resultSet = statement.getResultSet()) {
                    assertThat("Should not have found Student " + id + " in DB", resultSet.next(), is(false));
                }
            }

            String addressSql = String.format("select * from addresses where roll = '%s'", id.getRollNumber());
            try (PreparedStatement statement = conn.prepareStatement(addressSql)) {
                statement.execute();
                try (ResultSet resultSet = statement.getResultSet()) {
                    assertThat("Should not have found Address " + id + " in DB", resultSet.next(), is(false));
                }
            }
        }
    }

    void deleteUser(String roll) throws SQLException {
        try (Connection conn = dataSource.getConnection(testUser, testPassword)) {
            String deleteStudentSql = String.format("delete from students where roll = '%s'", roll);
            try (PreparedStatement statement = conn.prepareStatement(deleteStudentSql)) {
                statement.execute();
            }

            String deleteAddressSql = String.format("delete from addresses where roll = '%s'", roll);
            try (PreparedStatement statement = conn.prepareStatement(deleteAddressSql)) {
                statement.execute();
            }

        }
    }
}
