package com.fallt.orders.service;

import com.fallt.orders.domain.dto.SuccessResponse;
import com.fallt.orders.domain.dto.UpsertOrderRequest;
import com.fallt.orders.domain.entity.Order;
import com.fallt.orders.mapper.OrderMapper;
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
public class OrderService {

    @Value("${app.kafka.orderTopic}")
    private String orderTopicName;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;

    public SuccessResponse create(UpsertOrderRequest request) {
        Order order = orderMapper.toEntity(request);
        order.setStatus("NEW");
        log.info("Saved orders to database: {}", order);
        try {
            String message = objectMapper.writeValueAsString(order);
            kafkaTemplate.send(orderTopicName, message);
            log.info("Message: {} sent to topic: {}", message, orderTopicName);
        } catch (JsonProcessingException e) {
            log.error("Error converting order to json", e);
        }
        return SuccessResponse.builder()
                .message("your order has been processed")
                .build();
    }
}
