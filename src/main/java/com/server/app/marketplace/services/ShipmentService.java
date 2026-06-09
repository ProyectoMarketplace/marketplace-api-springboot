package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.CalculateShipmentRequest;
import com.server.app.marketplace.domain.dto.request.CreateShipmentRequest;
import com.server.app.marketplace.domain.dto.request.MarkOrderDeliveredRequest;
import com.server.app.marketplace.domain.dto.response.shipping.ShipmentCostResponse;
import com.server.app.marketplace.domain.dto.response.shipping.ShipmentResponse;

public interface ShipmentService {

    ShipmentCostResponse calculateShipment(CalculateShipmentRequest request);

    ShipmentResponse createShipment(CreateShipmentRequest request);

    ShipmentResponse getShipmentByOrderId(Long orderId);

    ShipmentResponse markOrderAsDelivered(Long orderId, MarkOrderDeliveredRequest request);
}