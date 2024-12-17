package com.fallt.orders.controller;

import com.fallt.orders.domain.dto.SuccessResponse;
import com.fallt.orders.domain.dto.UpsertOrderRequest;
import com.fallt.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public SuccessResponse create(@RequestBody UpsertOrderRequest request) {
        return orderService.create(request);
    }
}
