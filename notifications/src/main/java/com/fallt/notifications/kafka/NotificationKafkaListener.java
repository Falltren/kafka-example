package com.fallt.notifications.kafka;

import com.fallt.notifications.domain.entity.Order;
import com.fallt.notifications.service.NotificationService;
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
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${app.kafka.sentOrdersTopic}",
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
        log.info("Received order from sent_orders: {}", orderJson);
        log.info("Partition number is: {}", message.partition());
        try {
            Order order = objectMapper.readValue(orderJson, Order.class);
            notificationService.sendNotification(order);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON to Order", e);
        }
    }
}
