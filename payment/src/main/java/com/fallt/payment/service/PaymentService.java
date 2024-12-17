package com.fallt.payment.service;

import com.fallt.payment.domain.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    @Value("${app.kafka.paymentTopic}")
    private String paymentTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void payment(Order order) {
        log.info("payment processing started");
        try {
            sleep(3000);
            order.setStatus("PAYED");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("payment processing completed");
        try {
            kafkaTemplate.send(paymentTopic, objectMapper.writeValueAsString(order));
            log.info("Message {} sent to topic: {}", order, paymentTopic);
        } catch (JsonProcessingException e) {
            log.error("Error converting order to json", e);
        }
    }
}
