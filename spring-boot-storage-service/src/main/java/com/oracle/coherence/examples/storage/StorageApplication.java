package com.oracle.coherence.examples.storage;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.ConfigurableCacheFactory;
import com.tangosol.net.DefaultCacheServer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author Jonathan Knight  2020.09.10
 */
@SpringBootApplication
@EntityScan("com.oracle.coherence.examples.domain")  // JPA entities are in another package
public class StorageApplication {

    public static void main(String[] args) {
   		SpringApplication.run(StorageApplication.class, args);
   	}

	@Bean
 	public CommandLineRunner runCoherence(ApplicationContext ctx) {
		return (args) -> {
			// Create the default ConfigurableCacheFactory
			ConfigurableCacheFactory ccf = CacheFactory.getConfigurableCacheFactory();
			// Store the Spring ApplicationContext into the ConfigurableCacheFactory resource registry
			// so that it is accessible later - for example when creating the CacheStore
			ccf.getResourceRegistry().registerResource(ApplicationContext.class, ctx);
	        // Start Coherence server - this is a blocking call
			DefaultCacheServer.main(args);
		};
	}
}
