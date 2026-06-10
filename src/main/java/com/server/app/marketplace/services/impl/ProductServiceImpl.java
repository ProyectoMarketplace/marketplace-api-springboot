package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.mappers.ProductMapper;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.domain.dto.request.CreateProductRequest;
import com.server.app.marketplace.domain.dto.request.UpdateProductPriceRequest;
import com.server.app.marketplace.domain.dto.response.product.ProductResponse;
import com.server.app.marketplace.domain.entities.User;
import com.server.app.marketplace.domain.entities.Category;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.SellerProfile;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.CategoryRepository;
import com.server.app.marketplace.repositories.ProductRepository;
import com.server.app.marketplace.repositories.SellerProfileRepository;
import com.server.app.marketplace.repositories.UserRepository;
import com.server.app.marketplace.services.PriceNotificationService;
import com.server.app.marketplace.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final SellerProfileRepository sellerProfileRepository;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    private final UserRepository userRepository;

    private final PriceNotificationService priceNotificationService;

    @Override
    @Transactional
    public ProductResponse createProduct(
            Long sellerUserId,
            CreateProductRequest request
    ) {
        SellerProfile sellerProfile = sellerProfileRepository.findByUserId(sellerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        if (!category.getActive()) {
            throw new BusinessRuleException("Category is not active.");
        }

        Product product = productMapper.toEntity(request, sellerProfile, category);
        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllApprovedProducts() {
        return productRepository.findByStatus(ProductStatus.APPROVED)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse getProductById(Long id) {
        Product product = findProduct(id);

        if (product.getStatus() == ProductStatus.APPROVED) {
            product.setViews(product.getViews() + 1);
            productRepository.save(product);
        }

        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductResponse approveProduct(Long id) {
        Product product = findProduct(id);

        if (product.getStock() == 0) {
            product.setStatus(ProductStatus.SOLD_OUT);
            Product updatedProduct = productRepository.save(product);
            return productMapper.toDto(updatedProduct);
        }

        product.setStatus(ProductStatus.APPROVED);
        Product updatedProduct = productRepository.save(product);

        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponse rejectProduct(Long id) {
        Product product = findProduct(id);

        product.setStatus(ProductStatus.REJECTED);
        Product updatedProduct = productRepository.save(product);

        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProductPrice(Long id, UpdateProductPriceRequest request) {
        User seller = userRepository.findById(request.getSellerUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!Boolean.TRUE.equals(seller.getActive())) {
            throw new BusinessRuleException("User is inactive.");
        }

        if (seller.getRole() != UserRole.SELLER) {
            throw new BusinessRuleException("Only SELLER users can update product prices.");
        }

        Product product = findProduct(id);

        if (!product.getSellerProfile().getUser().getId().equals(seller.getId())) {
            throw new BusinessRuleException("This product does not belong to the seller.");
        }

        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new BusinessRuleException("Only APPROVED products can update price.");
        }

        Double previousPrice = product.getPrice();
        Double newPrice = request.getNewPrice();

        if (newPrice.equals(previousPrice)) {
            throw new BusinessRuleException("New price must be different from the current price.");
        }

        product.setPrice(newPrice);
        Product updatedProduct = productRepository.save(product);

        priceNotificationService.processPriceDrop(updatedProduct, previousPrice, newPrice);

        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponse deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.deleteById(id);

        return productMapper.toDto(product);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }
}