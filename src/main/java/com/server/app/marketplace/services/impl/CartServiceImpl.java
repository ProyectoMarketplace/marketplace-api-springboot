package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.CartMapper;
import com.server.app.marketplace.domain.dto.request.AddCartItemRequest;
import com.server.app.marketplace.domain.dto.request.UpdateCartItemRequest;
import com.server.app.marketplace.domain.dto.response.cart.CartResponse;
import com.server.app.marketplace.domain.entities.Cart;
import com.server.app.marketplace.domain.entities.CartItem;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.User;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.CartItemRepository;
import com.server.app.marketplace.repositories.CartRepository;
import com.server.app.marketplace.repositories.ProductRepository;
import com.server.app.marketplace.repositories.UserRepository;
import com.server.app.marketplace.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartResponse addItem(Long buyerId, AddCartItemRequest request) {
        User buyer = findBuyer(buyerId);
        Product product = findProduct(request.getProductId());

        validateProductCanBeAdded(product, request.getQuantity());

        Cart cart = getOrCreateCart(buyer);

        CartItem item = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(request.getQuantity())
                .unitPrice(product.getPrice())
                .build();

        cart.getItems().add(item);

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toDto(savedCart);
    }

    @Override
    public CartResponse getCart(Long buyerId) {
        findBuyer(buyerId);

        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found."));

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartResponse updateItem(Long buyerId, Long itemId, UpdateCartItemRequest request) {
        findBuyer(buyerId);

        CartItem item = cartItemRepository.findByIdAndCartBuyerId(itemId, buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found."));

        validateProductCanBeAdded(item.getProduct(), request.getQuantity());

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        Cart cart = item.getCart();

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartResponse removeItem(Long buyerId, Long itemId) {
        findBuyer(buyerId);

        CartItem item = cartItemRepository.findByIdAndCartBuyerId(itemId, buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found."));

        Cart cart = item.getCart();
        cart.getItems().remove(item);

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toDto(savedCart);
    }

    @Override
    @Transactional
    public CartResponse clearCart(Long buyerId) {
        findBuyer(buyerId);

        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found."));

        cart.getItems().clear();

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toDto(savedCart);
    }

    private User findBuyer(Long buyerId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        if (buyer.getRole() != UserRole.BUYER) {
            throw new BusinessRuleException("Only BUYER users can use the cart.");
        }

        return buyer;
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }

    private void validateProductCanBeAdded(Product product, Integer quantity) {
        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new BusinessRuleException("Only approved products can be added to the cart.");
        }

        if (product.getStock() < quantity) {
            throw new BusinessRuleException("Not enough stock available.");
        }
    }

    private Cart getOrCreateCart(User buyer) {
        return cartRepository.findByBuyerId(buyer.getId())
                .orElseGet(() -> Cart.builder()
                        .buyer(buyer)
                        .active(true)
                        .items(new ArrayList<>())
                        .build());
    }
}