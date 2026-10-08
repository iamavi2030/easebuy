package com.substring.easybuy.products.service.impl;

import com.substring.easybuy.products.dtos.ProductRequestDto;
import com.substring.easybuy.products.dtos.ProductResponseDto;
import com.substring.easybuy.products.entity.Category;
import com.substring.easybuy.products.entity.Product;
import com.substring.easybuy.products.exception.ResourceNotFoundException;
import com.substring.easybuy.products.repository.CategoryRepository;
import com.substring.easybuy.products.repository.ProductRepository;
import com.substring.easybuy.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
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

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponseDto(product);
    }

    @Override
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

    @Override
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponseDto addCategoryToProduct(UUID productId, Long categoryId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        if (!product.getCategories().contains(category)) {
            product.getCategories().add(category);
            productRepository.save(product);
        }
        return mapToResponseDto(product);
    }

    @Override
    @Transactional
    public ProductResponseDto removeCategoryFromProduct(UUID productId, Long categoryId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        product.getCategories().removeIf(c -> c.getId().equals(categoryId));
        productRepository.save(product);
        return mapToResponseDto(product);
    }

    @Override
    @Transactional
    public ProductResponseDto addProductImages(UUID productId, List<String> imageUrls) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (imageUrls != null && !imageUrls.isEmpty()) {
            product.getProductImages().addAll(imageUrls);
            productRepository.save(product);
        }
        return mapToResponseDto(product);
    }

    @Override
    public List<String> getProductImages(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return product.getProductImages();
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
