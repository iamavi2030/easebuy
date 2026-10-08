package com.substring.easybuy.products.controller;

import com.substring.easybuy.products.dtos.ProductRequestDto;
import com.substring.easybuy.products.dtos.ProductResponseDto;
import com.substring.easybuy.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto createdProduct = productService.createProduct(productRequestDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, productRequestDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductResponseDto> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/{productId}/categories/{categoryId}")
    public ResponseEntity<ProductResponseDto> addCategoryToProduct(
            @PathVariable UUID productId,
            @PathVariable Long categoryId) {
        ProductResponseDto product = productService.addCategoryToProduct(productId, categoryId);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}/categories/{categoryId}")
    public ResponseEntity<ProductResponseDto> removeCategoryFromProduct(
            @PathVariable UUID productId,
            @PathVariable Long categoryId) {
        ProductResponseDto product = productService.removeCategoryFromProduct(productId, categoryId);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/{productId}/images")
    public ResponseEntity<ProductResponseDto> addProductImages(
            @PathVariable UUID productId,
            @RequestBody List<String> imageUrls) {
        ProductResponseDto product = productService.addProductImages(productId, imageUrls);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<String>> getProductImages(@PathVariable UUID productId) {
        List<String> images = productService.getProductImages(productId);
        return ResponseEntity.ok(images);
    }
}
