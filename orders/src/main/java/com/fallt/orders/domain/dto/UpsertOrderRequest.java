package com.fallt.orders.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertOrderRequest {

    private List<String> products = new ArrayList<>();
    private Double cost;
}
