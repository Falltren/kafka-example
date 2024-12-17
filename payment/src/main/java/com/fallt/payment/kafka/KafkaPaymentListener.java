package com.fallt.payment.kafka;

import com.fallt.payment.domain.entity.Order;
import com.fallt.payment.service.PaymentService;
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
public class KafkaPaymentListener {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "new_orders", containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void processOrder(ConsumerRecord<String, String> message) {
        String orderJson = message.value();
        log.info("Received order from new_orders: {}", orderJson);

        try {
            Order order = objectMapper.readValue(orderJson, Order.class);
            paymentService.payment(order);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON to Order", e);
        }
    }
}
