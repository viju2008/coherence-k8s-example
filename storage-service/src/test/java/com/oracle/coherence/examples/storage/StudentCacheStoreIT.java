package com.oracle.coherence.examples.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.oracle.coherence.examples.domain.Address;
import com.oracle.coherence.examples.domain.Student;
import com.oracle.coherence.examples.domain.StudentId;

import oracle.jdbc.pool.OracleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Jonathan Knight  2020.09.09
 */
public class StudentCacheStoreIT {

    private static DBUtils db;

    @BeforeAll
    public static void setup() throws SQLException {
        db = new DBUtils();
    }

    @Test
    public void shouldStoreLoadAndDeleteStudent() throws Exception {
        String roll = "abcd-1234";
        db.deleteUser(roll);

        Address address = new Address();
        address.setLineOne("Freeda Apartments");
        address.setLineTwo("Carter Road, Bandra West");
        address.setCity("Mumbai");
        address.setPostalCode("123456");
        address.setCountry("India");

        StudentId id = new StudentId(roll);
        Student student = new Student(roll,"Aamir", "Khan", "drama", address);

        StudentCacheStore store = new StudentCacheStore(db.getDataSource(), db.getTestUser(), db.getTestPassword());

        // Should not be present in DB
        Student loaded = store.load(id);
        assertThat(loaded, is(nullValue()));

        // Save to DB through the CacheStore
        store.store(id, student);
        db.assertStudentInDB(id, student);

        loaded = store.load(id);
        assertThat(loaded, is(notNullValue()));
        assertThat(loaded.getRollNumber(), is(student.getRollNumber()));
        assertThat(loaded.getFirstName(), is(student.getFirstName()));
        assertThat(loaded.getLastName(), is(student.getLastName()));
        assertThat(loaded.getClassName(), is(student.getClassName()));
        Address loadedAddress = loaded.getAddress();
        assertThat(loadedAddress.getLineOne(), is(address.getLineOne()));
        assertThat(loadedAddress.getLineTwo(), is(address.getLineTwo()));
        assertThat(loadedAddress.getCity(), is(address.getCity()));
        assertThat(loadedAddress.getPostalCode(), is(address.getPostalCode()));
        assertThat(loadedAddress.getCountry(), is(address.getCountry()));

        // Should delete Student
        store.erase(id);
        db.assertStudentNotInDB(id);

        // Stundent load should now be null
        loaded = store.load(id);
        assertThat(loaded, is(nullValue()));
    }
}
