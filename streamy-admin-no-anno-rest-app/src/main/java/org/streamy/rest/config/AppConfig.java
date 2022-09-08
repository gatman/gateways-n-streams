package org.streamy.rest.config;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.HeaderEnricherSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.support.channel.HeaderChannelRegistry;
import org.springframework.messaging.Message;
import org.streamy.model.SampleDetail;

import lombok.extern.slf4j.XSlf4j;

@Configuration
@IntegrationComponentScan
@XSlf4j
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

    public interface GetSampleDetails extends Function<String, List<SampleDetail>> {
    }

    public interface MessageConsumer extends Consumer<Message<List<SampleDetail>>> {
    }
    
    @Bean
    public IntegrationFlow requestFlow(StreamBridge streamBridge) {
        return IntegrationFlows.from(GetSampleDetails.class)
                               .enrichHeaders(HeaderEnricherSpec::headerChannelsToString)
                               .handle(m -> streamBridge.send("sample-details-req", m))
                               .get();
    }

    @Bean
    IntegrationFlow getSampleDetailsFlow(HeaderChannelRegistry channelRegistry) {
        return IntegrationFlows.from(MessageConsumer.class, gateway -> gateway.beanName("getSampleDetails"))
                               .filter(Message.class, m -> channelRegistry.channelNameToChannel(m.getHeaders()
                                                                                                 .getReplyChannel()
                                                                                                 .toString()) != null,
                                       filterEndpointSpec -> filterEndpointSpec.throwExceptionOnRejection(true))
                               .get();
    }

}
