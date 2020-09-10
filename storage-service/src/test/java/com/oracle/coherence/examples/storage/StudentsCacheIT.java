package com.oracle.coherence.examples.storage;

import java.sql.SQLException;
import java.util.UUID;

import com.oracle.coherence.examples.domain.Address;
import com.oracle.coherence.examples.domain.Student;
import com.oracle.coherence.examples.domain.StudentId;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.ConfigurableCacheFactory;
import com.tangosol.net.DefaultCacheServer;
import com.tangosol.net.NamedCache;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Jonathan Knight  2020.09.09
 */
public class StudentsCacheIT {

    private static DBUtils db;

    private static ConfigurableCacheFactory ccf;

    @BeforeAll
    static void setup() throws SQLException {
        db = new DBUtils();
        ccf = CacheFactory.getConfigurableCacheFactory();
        DefaultCacheServer.startServerDaemon(ccf);
    }

    @AfterAll
    static void tearDown() {
        DefaultCacheServer.shutdown();
    }

    @Test
    public void shouldAccessStudentCache() throws SQLException {
        // ensure a unique test roll by using a UUID.
        String roll = UUID.randomUUID().toString();
        StudentId id = new StudentId(roll);

        Address address = new Address();
        address.setLineOne("Freeda Apartments");
        address.setLineTwo("Carter Road, Bandra West");
        address.setCity("Mumbai");
        address.setPostalCode("123456");
        address.setCountry("India");

        Student student = new Student(roll,"Aamir", "Khan", "drama", address);

        NamedCache<StudentId, Student> cache = ccf.ensureCache("students", null);
        // ensure that the cache is empty
        cache.clear();

        // Do a get, which should return null as the Student is not in the DB
        Student cachedStudent = cache.get(id);
        assertThat(cachedStudent, is(nullValue()));

        // put into the cache - should write through to DB
        cache.put(id, student);
        db.assertStudentInDB(id, student);

        // Do a get, which should read from the DB into the cache
        cachedStudent = cache.get(id);
        assertThat(cachedStudent, is(notNullValue()));
        assertThat(cache.size(), is(1));

        // evict from the cache - this WILL NOT delete from the DB but will remove from the cache.
        cache.invoke(id, entry -> {
            entry.remove(true);
            return null;
        });
        assertThat(cache.size(), is(0));

        // Student should still be in the DB
        db.assertStudentInDB(id, student);

        // Do a get, which should read from the DB into the cache
        cachedStudent = cache.get(id);
        assertThat(cache.size(), is(1));
        assertThat(cachedStudent, is(notNullValue()));
        assertThat(cachedStudent.getFirstName(), is(student.getFirstName()));
        assertThat(cachedStudent.getLastName(), is(student.getLastName()));
        assertThat(cachedStudent.getClassName(), is(student.getClassName()));
        assertThat(cachedStudent.getAddress().getLineOne(), is(student.getAddress().getLineOne()));
        assertThat(cachedStudent.getAddress().getLineTwo(), is(student.getAddress().getLineTwo()));
        assertThat(cachedStudent.getAddress().getCity(), is(student.getAddress().getCity()));
        assertThat(cachedStudent.getAddress().getPostalCode(), is(student.getAddress().getPostalCode()));
        assertThat(cachedStudent.getAddress().getCountry(), is(student.getAddress().getCountry()));

        // Do an update
        student.setClassName("business");
        cache.put(id, student);
        db.assertStudentInDB(id, student);

        cachedStudent = cache.get(id);
        assertThat(cachedStudent, is(notNullValue()));
        assertThat(cachedStudent.getClassName(), is("business"));

        // do a delete, which should remove from the DB
        cache.remove(id);
        db.assertStudentNotInDB(id);
    }

}
