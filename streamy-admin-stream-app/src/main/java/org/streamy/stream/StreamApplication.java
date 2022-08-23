package org.streamy.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.XSlf4j;

@SpringBootApplication
@XSlf4j
public class StreamApplication {
	public static void main(String[] args) {
		log.entry(args);
		SpringApplication.run(StreamApplication.class, args);
	}
}