package com.onegini.bananaisland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservice with REST interface to perform basic banking operations.
 *
 * @author Adrian Sidor
 */
@SpringBootApplication
public class BananaIslandApplication {

	public static void main(String[] args) {
		SpringApplication.run(BananaIslandApplication.class, args);
	}

}
