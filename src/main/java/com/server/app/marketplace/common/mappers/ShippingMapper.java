package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.request.ShippingMethodRequest;
import com.server.app.marketplace.domain.dto.response.shipping.ShipmentCostResponse;
import com.server.app.marketplace.domain.dto.response.shipping.ShipmentResponse;
import com.server.app.marketplace.domain.dto.response.shipping.ShippingMethodResponse;
import com.server.app.marketplace.domain.entities.Order;
import com.server.app.marketplace.domain.entities.Shipment;
import com.server.app.marketplace.domain.entities.ShippingMethod;
import org.springframework.stereotype.Component;

@Component
public class ShippingMapper {

    public ShippingMethod toEntity(ShippingMethodRequest request) {
        return ShippingMethod.builder()
                .name(request.getName())
                .provider(request.getProvider())
                .baseCost(request.getBaseCost())
                .active(true)
                .build();
    }

    public ShippingMethodResponse toDto(ShippingMethod method) {
        return ShippingMethodResponse.builder()
                .id(method.getId())
                .name(method.getName())
                .provider(method.getProvider())
                .baseCost(method.getBaseCost())
                .active(method.getActive())
                .build();
    }

    public ShipmentCostResponse toCostDto(Order order, ShippingMethod method, Double shippingCost) {
        return ShipmentCostResponse.builder()
                .orderId(order.getId())
                .shippingMethodId(method.getId())
                .shippingMethod(method.getName())
                .provider(method.getProvider())
                .orderTotal(order.getTotal())
                .shippingCost(shippingCost)
                .finalTotal(order.getTotal() + shippingCost)
                .build();
    }

    public ShipmentResponse toShipmentDto(Shipment shipment) {
        return ShipmentResponse.builder()
                .id(shipment.getId())
                .orderId(shipment.getOrder().getId())
                .shippingMethodId(shipment.getShippingMethod().getId())
                .shippingMethod(shipment.getShippingMethod().getName())
                .provider(shipment.getShippingMethod().getProvider())
                .cost(shipment.getCost())
                .status(shipment.getStatus())
                .trackingCode(shipment.getTrackingCode())
                .build();
    }
}