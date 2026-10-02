package com.substring.easybuy.products.service;

import com.substring.easybuy.products.dtos.ProductRequestDto;
import com.substring.easybuy.products.dtos.ProductResponseDto;
import com.substring.easybuy.products.entity.Category;
import com.substring.easybuy.products.entity.Product;
import com.substring.easybuy.products.exception.ResourceNotFoundException;
import com.substring.easybuy.products.repository.CategoryRepository;
import com.substring.easybuy.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setTitle(productRequestDto.getTitle());
        product.setShortDesc(productRequestDto.getShortDesc());
        product.setLongDesc(productRequestDto.getLongDesc());
        product.setPrice(productRequestDto.getPrice());
        product.setDiscount(productRequestDto.getDiscount());
        product.setLive(productRequestDto.getLive() != null ? productRequestDto.getLive() : false);
        product.setProductImages(productRequestDto.getProductImages());

        if (productRequestDto.getCategoryIds() != null && !productRequestDto.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(productRequestDto.getCategoryIds());
            if (categories.size() != productRequestDto.getCategoryIds().size()) {
                throw new ResourceNotFoundException("One or more categories not found");
            }
            product.setCategories(categories);
        }

        Product savedProduct = productRepository.save(product);
        return mapToResponseDto(savedProduct);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(UUID id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        product.setTitle(productRequestDto.getTitle());
        product.setShortDesc(productRequestDto.getShortDesc());
        product.setLongDesc(productRequestDto.getLongDesc());
        product.setPrice(productRequestDto.getPrice());
        product.setDiscount(productRequestDto.getDiscount());
        product.setLive(productRequestDto.getLive() != null ? productRequestDto.getLive() : product.isLive());
        
        if (productRequestDto.getProductImages() != null) {
            product.setProductImages(productRequestDto.getProductImages());
        }

        if (productRequestDto.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(productRequestDto.getCategoryIds());
            if (!productRequestDto.getCategoryIds().isEmpty() && 
                categories.size() != productRequestDto.getCategoryIds().size()) {
                throw new ResourceNotFoundException("One or more categories not found");
            }
            product.setCategories(categories);
        }

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDto(updatedProduct);
    }

    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private ProductResponseDto mapToResponseDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setShortDesc(product.getShortDesc());
        dto.setLongDesc(product.getLongDesc());
        dto.setPrice(product.getPrice());
        dto.setDiscount(product.getDiscount());
        dto.setLive(product.isLive());
        dto.setProductImages(product.getProductImages());
        
        dto.setCategories(product.getCategories().stream()
                .map(category -> {
                    com.substring.easybuy.products.dtos.CategoryResponseDto categoryDto = new com.substring.easybuy.products.dtos.CategoryResponseDto();
                    categoryDto.setId(category.getId());
                    categoryDto.setTitle(category.getTitle());
                    return categoryDto;
                })
                .collect(Collectors.toList()));
        
        dto.setReviews(product.getReviews().stream()
                .map(review -> {
                    com.substring.easybuy.products.dtos.ReviewResponseDto reviewDto = new com.substring.easybuy.products.dtos.ReviewResponseDto();
                    reviewDto.setId(review.getId());
                    reviewDto.setTitle(review.getTitle());
                    reviewDto.setComment(review.getComment());
                    reviewDto.setRating(review.getRating());
                    reviewDto.setProductId(review.getProduct() != null ? review.getProduct().getId() : null);
                    return reviewDto;
                })
                .collect(Collectors.toList()));
        
        return dto;
    }
}
