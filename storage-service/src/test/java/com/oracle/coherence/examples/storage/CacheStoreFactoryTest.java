package com.oracle.coherence.examples.storage;

import com.tangosol.net.cache.CacheStore;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Jonathan Knight  2020.09.09
 */
public class CacheStoreFactoryTest {

    @BeforeAll
    static void setup() {
        System.setProperty(StudentCacheStore.PROP_DB_HOST, "localhost");
        System.setProperty(StudentCacheStore.PROP_DB_PORT, "31521");
        System.setProperty(StudentCacheStore.PROP_DB_SID, "ORCLPDB1");
        System.setProperty(StudentCacheStore.PROP_DB_USER, "scott");
        System.setProperty(StudentCacheStore.PROP_DB_PWD, "tiger");
    }

    @Test
    void shouldCreateStudentCacheStore() throws Exception {
        CacheStore<?, ?> store = CacheStoreFactory.createCacheStore("students");
        assertThat(store, is(notNullValue()));
    }

    @Test
    void shouldNotCreateCacheStoreForUnknownCacheName() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> CacheStoreFactory.createCacheStore("foo"));
    }
}
