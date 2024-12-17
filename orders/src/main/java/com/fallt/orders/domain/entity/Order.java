package com.fallt.orders.domain.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {

    private Long id;
    private List<String> products = new ArrayList<>();
    private Boolean cost;
}
