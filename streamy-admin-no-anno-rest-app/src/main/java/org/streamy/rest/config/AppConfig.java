package org.streamy.rest.config;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.HeaderEnricherSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.support.channel.HeaderChannelRegistry;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.core.GenericMessagingTemplate;
import org.streamy.model.SampleDetail;

import lombok.extern.slf4j.XSlf4j;

@Configuration
@IntegrationComponentScan
@XSlf4j
public class AppConfig {

    // @Autowired
    // StreamBridge bridge;

    @Autowired
    HeaderChannelRegistry channelRegistry;
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

    // public interface ErrorMessageConsumer extends Consumer<Message<String>> {
    // }

    @Bean
    public IntegrationFlow requestFlow(StreamBridge streamBridge, HeaderChannelRegistry channelRegistry) {
        return IntegrationFlows.from(GetSampleDetails.class, gateway -> gateway.replyTimeout(20000))
                               .enrichHeaders(HeaderEnricherSpec::headerChannelsToString)
                               // .filter(Message.class, m ->
                               // channelRegistry.channelNameToChannel(m.getHeaders()
                               // .getErrorChannel()
                               // .toString()) != null,
                               // filterEndpointSpec ->
                               // filterEndpointSpec.throwExceptionOnRejection(true))
                               .handle(m -> streamBridge.send("sample-details-req", m))
                               .get();
    }

    @Bean
    IntegrationFlow getSampleDetailsFlow(HeaderChannelRegistry channelRegistry) {
        return IntegrationFlows.from(MessageConsumer.class, gateway -> gateway.beanName("getSampleDetails"))
                               .filter(Message.class, m -> channelRegistry.channelNameToChannel(m.getHeaders()
                                                                                                 .getReplyChannel()
                                                                                                 .toString()) != null,
                                       filterEndpointSpec -> filterEndpointSpec.throwExceptionOnRejection(true)
                                                                               .requiresReply(true))
                               .get();
    }

    // @Bean
    // IntegrationFlow getSampleDetailsErrorFlow(HeaderChannelRegistry
    // channelRegistry) {
    // return IntegrationFlows.from("getSampleDetailsErrors")
    // .filter(Message.class, m ->
    // channelRegistry.channelNameToChannel(m.getHeaders()
    // .getErrorChannel()
    // .toString()) != null,
    // filterEndpointSpec -> filterEndpointSpec.throwExceptionOnRejection(true))
    // .handle(m -> {
    // log.error("Received error: H:{} P:{}", m.getHeaders()
    // .toString(),
    // m.getPayload()
    // .toString());
    // })
    // .get();
    // }

    @RabbitListener(queues = {"sample-details-req.get_sampl_dets.dlq"} )
    public void processDlqMessage(Message dlqMessage) {
        log.info("dlq message payload: {}", dlqMessage.getPayload());
        log.info("dlq message headers: {}", dlqMessage.getHeaders()
                                                      .toString());
        MessageChannel errorChan = channelRegistry.channelNameToChannel(dlqMessage.getHeaders()
                                                                                  .getErrorChannel()
                                                                                  .toString());
        MessageChannel replyChan = channelRegistry.channelNameToChannel(dlqMessage.getHeaders()
                                                                                  .getReplyChannel()
                                                                                  .toString());
        // MessageChannel replyChan =
        // channelRegistry.channelNameToChannel("sample-details-resp");

        // Message errMessage = MessageBuilder.fromMessage(dlqMessage)
        // .withPayload(null)
        // .build();
        errorChan.send(dlqMessage);
    }
}
