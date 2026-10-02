package com.substring.easybuy.products.service;

import com.substring.easybuy.products.dtos.CategoryRequestDto;
import com.substring.easybuy.products.dtos.CategoryResponseDto;
import com.substring.easybuy.products.entity.Category;
import com.substring.easybuy.products.exception.ResourceNotFoundException;
import com.substring.easybuy.products.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setTitle(categoryRequestDto.getTitle());
        Category savedCategory = categoryRepository.save(category);
        return mapToResponseDto(savedCategory);
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToResponseDto(category);
    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setTitle(categoryRequestDto.getTitle());
        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDto(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    private CategoryResponseDto mapToResponseDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());
        dto.setProducts(category.getProducts().stream()
                .map(product -> {
                    com.substring.easybuy.products.dtos.ProductResponseDto productDto = new com.substring.easybuy.products.dtos.ProductResponseDto();
                    productDto.setId(product.getId());
                    productDto.setTitle(product.getTitle());
                    productDto.setShortDesc(product.getShortDesc());
                    productDto.setLongDesc(product.getLongDesc());
                    productDto.setPrice(product.getPrice());
                    productDto.setDiscount(product.getDiscount());
                    productDto.setLive(product.isLive());
                    productDto.setProductImages(product.getProductImages());
                    return productDto;
                })
                .collect(Collectors.toList()));
        return dto;
    }
}
