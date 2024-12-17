package com.fallt.orders.domain.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Order {

    private String uuid = UUID.randomUUID().toString();
    private List<String> products = new ArrayList<>();
    private String status;
    private Double cost;
}
