package com.fallt.shipping.kafka;

import com.fallt.shipping.domain.entity.Order;
import com.fallt.shipping.service.ShippingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShippingKafkaListener {

    private final ObjectMapper objectMapper;
    private final ShippingService shippingService;

    @KafkaListener(topics = "${app.kafka.paymentTopic}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            concurrency = "3")
    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 5000),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltStrategy = DltStrategy.NO_DLT,
            exclude = NullPointerException.class, traversingCauses = "true"
    )
    public void processOrder(ConsumerRecord<String, String> message) {
        String orderJson = message.value();
        log.info("Received order from payed_orders: {}", orderJson);
        try {
            Order order = objectMapper.readValue(orderJson, Order.class);
            shippingService.sentOrder(order);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON to Order", e);
        }
    }
}
