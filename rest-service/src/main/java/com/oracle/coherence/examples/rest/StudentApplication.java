package com.oracle.coherence.examples.rest;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.ConfigurableCacheFactory;
import com.tangosol.net.Session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Jonathan Knight  2020.09.10
 */
@SpringBootApplication
public class StudentApplication {

    public static void main(String[] args) {
   		SpringApplication.run(StudentApplication.class, args);
   	}

   	@Bean
    public Session createCCF() {
        return Session.create();
    }
}
