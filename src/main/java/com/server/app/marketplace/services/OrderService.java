package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.response.order.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse checkout(Long buyerId);

    List<OrderResponse> getMyOrders(Long buyerId);

    OrderResponse getOrderById(Long id);
}