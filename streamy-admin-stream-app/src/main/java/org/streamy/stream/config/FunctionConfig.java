package org.streamy.stream.config;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.streamy.model.SampleDetail;

import lombok.extern.slf4j.XSlf4j;

@Configuration
@XSlf4j
public class FunctionConfig {
	
	@Bean
	Function<String, List<SampleDetail>> getSampleDetails() {
		return input -> {
			if (log.isInfoEnabled()) {
				log.info("getSampleDetails received: {}", input);
			}
			SampleDetail det = new SampleDetail();
			det.setRequestId(input);
			det.setApplication("Sample");
			det.setCreatedBy("Bond");
			det.setUpdatedBy("Q");
			det.setCreatedDate("01/01/2022");
			det.setName("name 1");
			det.setUUID("ABCD");
			det.setStatus("New");
			det.setUiVersion("1.2.3");
			det.setBooleanOne(true);
			det.setIntOne(1000);
			det.setDblOne(23.45);
			
			SampleDetail det2 = new SampleDetail();
			det2.setRequestId(input);
			det2.setApplication("Sample");
			det2.setCreatedBy("Q");
			det2.setUpdatedBy("Bond");
			det2.setCreatedDate("01/01/2022");
			det2.setName("name 2");
			det2.setUUID("EFGH");
			det2.setStatus("New");
			det2.setUiVersion("1.2.3");
            det2.setBooleanOne(false);
            det2.setIntOne(2000);
            det2.setDblOne(45.23);
			
			List<SampleDetail> ret = Arrays.asList(det, det2);
			if (log.isInfoEnabled()) {
			    log.info("returning: {}::{}", input, det2);
			}
			return ret;
		};
	}

}
