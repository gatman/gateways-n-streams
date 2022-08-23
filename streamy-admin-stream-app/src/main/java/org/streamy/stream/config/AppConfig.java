package org.streamy.stream.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import lombok.extern.slf4j.XSlf4j;

@Configuration
@Import({FunctionConfig.class})
@XSlf4j
public class AppConfig {

}
