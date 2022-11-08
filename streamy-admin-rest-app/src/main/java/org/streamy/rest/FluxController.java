package org.streamy.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/sampleDetails/{requestId}")
    public Mono<ResponseEntity<List<SampleDetail>>> getSampleDetails(@PathVariable("requestId") String requestId) {
//        String value = "Random Input: " + UUID.randomUUID()
//                                          .toString();
        log.info("Sending {} to requestResponseOne function", requestId);

        List<SampleDetail> response = sampleGateway.getSampleDetails(requestId);
        
        return Mono.just(ResponseEntity.ok(response));
    }
}
