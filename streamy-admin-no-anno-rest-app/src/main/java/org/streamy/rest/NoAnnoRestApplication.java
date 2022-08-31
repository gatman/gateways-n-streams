package org.streamy.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.XSlf4j;

@SpringBootApplication
@XSlf4j
public class NoAnnoRestApplication {
    

	public static void main(String[] args) {
		SpringApplication.run(NoAnnoRestApplication.class, args);
	}
	
}
