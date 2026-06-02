package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.SellerProfileMapper;
import com.server.app.marketplace.domain.dto.request.SellerProfileRequest;
import com.server.app.marketplace.domain.dto.response.seller.SellerProfileResponse;
import com.server.app.marketplace.domain.entities.SellerProfile;
import com.server.app.marketplace.domain.entities.User;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.SellerProfileRepository;
import com.server.app.marketplace.repositories.UserRepository;
import com.server.app.marketplace.services.SellerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerProfileServiceImpl implements SellerProfileService {

    private final SellerProfileRepository sellerProfileRepository;

    private final UserRepository userRepository;

    private final SellerProfileMapper sellerProfileMapper;

    @Override
    @Transactional
    public SellerProfileResponse createProfile(Long userId, SellerProfileRequest request) {
        User user = findUser(userId);

        if (user.getRole() != UserRole.SELLER) {
            throw new BusinessRuleException("Only users with SELLER role can create a seller profile.");
        }

        if (sellerProfileRepository.existsByUserId(userId)) {
            throw new BusinessRuleException("This user already has a seller profile.");
        }

        if (sellerProfileRepository.existsByStoreNameIgnoreCase(request.getStoreName())) {
            throw new BusinessRuleException("Store name is already registered.");
        }

        SellerProfile profile = sellerProfileMapper.toEntity(request, user);
        SellerProfile savedProfile = sellerProfileRepository.save(profile);

        return sellerProfileMapper.toDto(savedProfile);
    }

    @Override
    public SellerProfileResponse getProfileByUserId(Long userId) {
        SellerProfile profile = sellerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found."));

        return sellerProfileMapper.toDto(profile);
    }

    @Override
    @Transactional
    public SellerProfileResponse updateProfile(Long userId, SellerProfileRequest request) {
        SellerProfile profile = sellerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found."));

        if (!profile.getStoreName().equalsIgnoreCase(request.getStoreName())
                && sellerProfileRepository.existsByStoreNameIgnoreCase(request.getStoreName())) {
            throw new BusinessRuleException("Store name is already registered.");
        }

        sellerProfileMapper.updateEntity(profile, request);
        SellerProfile updatedProfile = sellerProfileRepository.save(profile);

        return sellerProfileMapper.toDto(updatedProfile);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }
}