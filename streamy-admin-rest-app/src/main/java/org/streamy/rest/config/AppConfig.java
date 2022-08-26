package org.streamy.rest.config;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.HeaderEnricherSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;
import org.streamy.model.SampleDetail;

import lombok.extern.slf4j.XSlf4j;
@Configuration
// @Import({SupplierConfig.class})
@XSlf4j
@IntegrationComponentScan
@EnableBinding(AppConfig.SampleGatewayChannels.class)
public class AppConfig {

    /**
     * TODO There was a name mismatch of the stream bridge. The one being
     * created was streamBridgeUtils. Other code was looking for streamBridge.
     * This seemed to solve that for now. Remove and double check that it's
     * needed once this mess works.
     * 
     * @param bridge
     * @return
     */
    @Bean
    public StreamBridge streamBridge(@Autowired StreamBridge bridge) {
        return bridge;
    }

    @MessagingGateway
    public interface SampleGateway {
        public static final String GATEWAY_CHAN_OUT = "gatway-out";
        public static final String GATEWAY_CHAN_IN = "gatway-in";

        @Gateway(requestChannel = GATEWAY_CHAN_OUT, replyChannel = GATEWAY_CHAN_IN, replyTimeout = 3000, requestTimeout = 3000)
        public List<SampleDetail> getSampleDetails(String input);

    }

    @Component
    interface SampleGatewayChannels {
        String GET_SAMPLE_DETAILS_OUT = "sample-details-req";
        String GET_SAMPLE_DETAILS_IN = "sample-details-resp";

        @Output(GET_SAMPLE_DETAILS_OUT)
        public SubscribableChannel getSampleDetailsOut();
        @Input(GET_SAMPLE_DETAILS_IN)
        public SubscribableChannel getSampleDetailsIn();
    }

    @Bean
    public IntegrationFlow headerEnricherFlow() {
        return IntegrationFlows.from(SampleGateway.GATEWAY_CHAN_OUT)
                               .enrichHeaders(HeaderEnricherSpec::headerChannelsToString)
                               .channel(SampleGatewayChannels.GET_SAMPLE_DETAILS_OUT)
                               .get();
    }

    @Bean
    public IntegrationFlow respFlow() {
        return IntegrationFlows.from(SampleGatewayChannels.GET_SAMPLE_DETAILS_IN)
                               .transform(Transformers.fromJson(
                                       ResolvableType.forClassWithGenerics(List.class, SampleDetail.class)))
                               .channel(SampleGateway.GATEWAY_CHAN_IN)
                               .get();
    }

    /*
     * Without a function of some sort being defined, a phantom function gets
     * mapped to /** and swagger doesn't work.
     */
    @Bean
    public Supplier<String> nadda() {
        return () -> {
            return "yo";
        };
    }
}
