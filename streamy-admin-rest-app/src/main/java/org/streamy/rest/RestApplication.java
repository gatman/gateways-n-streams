package org.streamy.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.dsl.IntegrationFlow;

import lombok.extern.slf4j.XSlf4j;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableSwagger2
//@EnableBinding(Processor.class)
@XSlf4j
public class RestApplication {
    

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}
	
}
