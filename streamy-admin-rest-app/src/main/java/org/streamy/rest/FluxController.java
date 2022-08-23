package org.streamy.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.streamy.model.SampleDetail;
import org.streamy.rest.config.AppConfig.SampleGateway;

import lombok.extern.slf4j.XSlf4j;
import reactor.core.publisher.Mono;

/**
 * Sample controller
 */
@RestController
@RequestMapping("/sample")
@XSlf4j
public class FluxController {
    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private SampleGateway sampleGateway;

    @GetMapping("/requestResponse")
    public Mono<ResponseEntity<List<SampleDetail>>> getEfforts() {
        String value = "Random Input: " + UUID.randomUUID()
                                          .toString();
        log.info("Sending {} to requestResponseOne function", value);

        List<SampleDetail> response = sampleGateway.getSampleDetails(value);
        
        return Mono.just(ResponseEntity.ok(response));
    }
}
