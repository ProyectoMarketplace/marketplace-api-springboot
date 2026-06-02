package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.OrderStatus;
import com.server.app.marketplace.common.enums.ShipmentStatus;
import com.server.app.marketplace.common.mappers.ShippingMapper;
import com.server.app.marketplace.domain.dto.request.CalculateShipmentRequest;
import com.server.app.marketplace.domain.dto.request.CreateShipmentRequest;
import com.server.app.marketplace.domain.dto.response.shipping.ShipmentCostResponse;
import com.server.app.marketplace.domain.dto.response.shipping.ShipmentResponse;
import com.server.app.marketplace.domain.entities.Order;
import com.server.app.marketplace.domain.entities.Shipment;
import com.server.app.marketplace.domain.entities.ShippingMethod;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.OrderRepository;
import com.server.app.marketplace.repositories.ShipmentRepository;
import com.server.app.marketplace.repositories.ShippingMethodRepository;
import com.server.app.marketplace.services.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final OrderRepository orderRepository;

    private final ShippingMethodRepository shippingMethodRepository;

    private final ShippingMapper shippingMapper;

    @Override
    public ShipmentCostResponse calculateShipment(CalculateShipmentRequest request) {
        Order order = findOrder(request.getOrderId());
        ShippingMethod method = findActiveMethod(request.getShippingMethodId());

        Double shippingCost = calculateCost(order, method);

        return shippingMapper.toCostDto(order, method, shippingCost);
    }

    @Override
    @Transactional
    public ShipmentResponse createShipment(CreateShipmentRequest request) {
        Order order = findOrder(request.getOrderId());
        ShippingMethod method = findActiveMethod(request.getShippingMethodId());

        if (order.getStatus() != OrderStatus.PAID) {
            throw new BusinessRuleException("Only PAID orders can generate shipments.");
        }

        if (shipmentRepository.existsByOrderId(order.getId())) {
            throw new BusinessRuleException("This order already has a shipment.");
        }

        Double shippingCost = calculateCost(order, method);

        Shipment shipment = Shipment.builder()
                .order(order)
                .shippingMethod(method)
                .cost(shippingCost)
                .status(ShipmentStatus.PENDING)
                .trackingCode(UUID.randomUUID().toString())
                .build();

        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);

        Shipment savedShipment = shipmentRepository.save(shipment);

        return shippingMapper.toShipmentDto(savedShipment);
    }

    @Override
    public ShipmentResponse getShipmentByOrderId(Long orderId) {
        Shipment shipment = shipmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found."));

        return shippingMapper.toShipmentDto(shipment);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
    }

    private ShippingMethod findActiveMethod(Long methodId) {
        ShippingMethod method = shippingMethodRepository.findById(methodId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping method not found."));

        if (!method.getActive()) {
            throw new BusinessRuleException("Shipping method is not active.");
        }

        return method;
    }

    private Double calculateCost(Order order, ShippingMethod method) {
        if (order.getTotal() >= 1000) {
            return method.getBaseCost() * 0.50;
        }

        if (order.getTotal() >= 500) {
            return method.getBaseCost() * 0.75;
        }

        return method.getBaseCost();
    }
}