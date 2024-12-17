package com.fallt.notifications.kafka;

import com.fallt.notifications.domain.entity.Order;
import com.fallt.notifications.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${app.kafka.sentOrdersTopic}", containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void processOrder(ConsumerRecord<String, String> message) {
        String orderJson = message.value();
        log.info("Received order from sent_orders: {}", orderJson);
        try {
            Order order = objectMapper.readValue(orderJson, Order.class);
            notificationService.sendNotification(order);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON to Order", e);
        }
    }
}
