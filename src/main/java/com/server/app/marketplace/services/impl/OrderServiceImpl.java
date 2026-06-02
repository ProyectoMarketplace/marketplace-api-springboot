package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.OrderStatus;
import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.OrderMapper;
import com.server.app.marketplace.domain.dto.response.order.OrderResponse;
import com.server.app.marketplace.domain.entities.*;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.CartRepository;
import com.server.app.marketplace.repositories.OrderRepository;
import com.server.app.marketplace.repositories.ProductRepository;
import com.server.app.marketplace.repositories.UserRepository;
import com.server.app.marketplace.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse checkout(Long buyerId) {
        User buyer = findBuyer(buyerId);

        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found."));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BusinessRuleException("Cart is empty.");
        }

        List<CartItem> cartItems = new ArrayList<>(cart.getItems());

        Order order = Order.builder()
                .buyer(buyer)
                .total(0.0)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        double total = 0.0;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            validateProductForCheckout(product, cartItem.getQuantity());

            double subtotal = product.getPrice() * cartItem.getQuantity();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(product.getPrice())
                    .subtotal(subtotal)
                    .build();

            order.getItems().add(orderItem);

            product.setStock(product.getStock() - cartItem.getQuantity());

            if (product.getStock() == 0) {
                product.setStatus(ProductStatus.SOLD_OUT);
            }

            productRepository.save(product);

            total += subtotal;
        }

        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderResponse> getMyOrders(Long buyerId) {
        findBuyer(buyerId);

        return orderRepository.findByBuyerId(buyerId)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        return orderMapper.toDto(order);
    }

    private User findBuyer(Long buyerId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        if (buyer.getRole() != UserRole.BUYER) {
            throw new BusinessRuleException("Only BUYER users can create orders.");
        }

        return buyer;
    }

    private void validateProductForCheckout(Product product, Integer quantity) {
        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new BusinessRuleException("Only approved products can be purchased.");
        }

        if (product.getStock() < quantity) {
            throw new BusinessRuleException("Not enough stock for product: " + product.getTitle());
        }
    }
}