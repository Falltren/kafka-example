package com.fallt.orders.mapper;

import java.util.List;

public interface Mappable<E, D, R> {

    E toEntity(D dto);

    R toResponse(E entity);

    List<R> toListResponse(List<E> entities);
}
