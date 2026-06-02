package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.ShippingMethodRequest;
import com.server.app.marketplace.domain.dto.response.shipping.ShippingMethodResponse;

import java.util.List;

public interface ShippingMethodService {

    ShippingMethodResponse createShippingMethod(ShippingMethodRequest request);

    List<ShippingMethodResponse> getActiveShippingMethods();
}