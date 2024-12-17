package com.fallt.orders.mapper;

import com.fallt.orders.domain.dto.SuccessResponse;
import com.fallt.orders.domain.dto.UpsertOrderRequest;
import com.fallt.orders.domain.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper extends Mappable<Order, UpsertOrderRequest, SuccessResponse> {
}
