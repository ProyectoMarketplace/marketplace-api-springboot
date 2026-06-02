package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.mappers.ShippingMapper;
import com.server.app.marketplace.domain.dto.request.ShippingMethodRequest;
import com.server.app.marketplace.domain.dto.response.shipping.ShippingMethodResponse;
import com.server.app.marketplace.domain.entities.ShippingMethod;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.repositories.ShippingMethodRepository;
import com.server.app.marketplace.services.ShippingMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingMethodServiceImpl implements ShippingMethodService {

    private final ShippingMethodRepository shippingMethodRepository;

    private final ShippingMapper shippingMapper;

    @Override
    @Transactional
    public ShippingMethodResponse createShippingMethod(ShippingMethodRequest request) {
        if (shippingMethodRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessRuleException("Shipping method name is already registered.");
        }

        ShippingMethod method = shippingMapper.toEntity(request);
        ShippingMethod savedMethod = shippingMethodRepository.save(method);

        return shippingMapper.toDto(savedMethod);
    }

    @Override
    public List<ShippingMethodResponse> getActiveShippingMethods() {
        return shippingMethodRepository.findByActiveTrue()
                .stream()
                .map(shippingMapper::toDto)
                .toList();
    }
}