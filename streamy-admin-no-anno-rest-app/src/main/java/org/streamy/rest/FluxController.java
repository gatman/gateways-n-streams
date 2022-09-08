package org.streamy.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    
    @GetMapping("/sampleDetails")
    public Mono<ResponseEntity<List<SampleDetail>>> getSampleDetails() {
        String value = "Random Input: " + UUID.randomUUID()
                                          .toString();
        log.info("Sending {} to getSampleDetails function", value);

        List<SampleDetail> response = getSampleDetailsGW.apply(value);
        
        return Mono.just(ResponseEntity.ok(response));
    }
    
    @EventListener
    public void onApplicationEvent(WebServerInitializedEvent event) {
        getSampleDetailsGW = appCtx.getBean(GetSampleDetails.class);    
    }
}
