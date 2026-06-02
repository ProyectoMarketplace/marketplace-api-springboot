package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.AddCartItemRequest;
import com.server.app.marketplace.domain.dto.request.UpdateCartItemRequest;
import com.server.app.marketplace.domain.dto.response.cart.CartResponse;

public interface CartService {

    CartResponse addItem(Long buyerId, AddCartItemRequest request);

    CartResponse getCart(Long buyerId);

    CartResponse updateItem(Long buyerId, Long itemId, UpdateCartItemRequest request);

    CartResponse removeItem(Long buyerId, Long itemId);

    CartResponse clearCart(Long buyerId);
}