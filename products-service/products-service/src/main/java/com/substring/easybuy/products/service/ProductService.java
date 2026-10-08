package com.substring.easybuy.products.service;

import com.substring.easybuy.products.dtos.ProductRequestDto;
import com.substring.easybuy.products.dtos.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    List<ProductResponseDto> getAllProducts();

    Page<ProductResponseDto> getAllProducts(Pageable pageable);

    ProductResponseDto getProductById(UUID id);

    ProductResponseDto updateProduct(UUID id, ProductRequestDto productRequestDto);

    void deleteProduct(UUID id);

    List<ProductResponseDto> getProductsByCategory(Long categoryId);

    ProductResponseDto addCategoryToProduct(UUID productId, Long categoryId);

    ProductResponseDto removeCategoryFromProduct(UUID productId, Long categoryId);

    ProductResponseDto addProductImages(UUID productId, List<String> imageUrls);

    List<String> getProductImages(UUID productId);
}
