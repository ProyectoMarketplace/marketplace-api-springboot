package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.response.cart.CartItemResponse;
import com.server.app.marketplace.domain.dto.response.cart.CartResponse;
import com.server.app.marketplace.domain.entities.Cart;
import com.server.app.marketplace.domain.entities.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartItemResponse toItemDto(CartItem item) {
        return CartItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productTitle(item.getProduct().getTitle())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getUnitPrice() * item.getQuantity())
                .build();
    }

    public CartResponse toDto(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems() == null
                ? List.of()
                : cart.getItems()
                .stream()
                .map(this::toItemDto)
                .toList();

        Double total = itemResponses.stream()
                .mapToDouble(CartItemResponse::getSubtotal)
                .sum();

        return CartResponse.builder()
                .id(cart.getId())
                .buyerId(cart.getBuyer().getId())
                .buyerName(cart.getBuyer().getFullName())
                .active(cart.getActive())
                .items(itemResponses)
                .total(total)
                .build();
    }
}