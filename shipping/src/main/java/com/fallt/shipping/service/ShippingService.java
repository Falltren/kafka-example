package com.fallt.shipping.service;

import com.fallt.shipping.domain.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {

    @Value("${app.kafka.sentOrdersTopic}")
    private String sentOrdersTopic;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sentOrder(Order order) {
        order.setStatus("SHIPPING");
        log.info("order was sent");
        try {
            String message = objectMapper.writeValueAsString(order);
            kafkaTemplate.send(sentOrdersTopic, message);
            log.info("Message {} sent to topic: {}", message, sentOrdersTopic);
        } catch (JsonProcessingException e) {
            log.error("Error converting order to json", e);
        }
    }
}
