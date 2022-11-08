package org.streamy.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.streamy.model.SampleDetail;
import org.streamy.rest.config.AppConfig.GetSampleDetails;

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
    private ApplicationContext appCtx;

    private GetSampleDetails getSampleDetailsGW;

    @GetMapping("/sampleDetails/{requestId}")
    public Mono<ResponseEntity<List<SampleDetail>>> getSampleDetails(@PathVariable("requestId") String requestId) {
        // String value = "Random Input: " + UUID.randomUUID()
        // .toString();
        log.info("Sending {} to getSampleDetails function", requestId);
        List<SampleDetail> response = getSampleDetailsGW.apply(requestId);
        /*
         * Can't get requiresResponse to work when timeout occurs. May have to
         * have one gateway consume another.
         */
        if (response == null) {
            throw new RuntimeException("No response from remote service");
        }

        return Mono.just(ResponseEntity.ok(response));
    }

    @EventListener
    public void onApplicationEvent(WebServerInitializedEvent event) {
        getSampleDetailsGW = appCtx.getBean(GetSampleDetails.class);
    }
}
