package com.fallt.payment.domain.entity;

import lombok.Data;

import java.util.List;

@Data
public class Order {

    private String uuid;
    private List<String> products;
    private String status;
    private Double cost;
}
