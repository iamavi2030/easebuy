package com.substring.easybuy.products.service.impl;

import com.substring.easybuy.products.dtos.CategoryRequestDto;
import com.substring.easybuy.products.dtos.CategoryResponseDto;
import com.substring.easybuy.products.entity.Category;
import com.substring.easybuy.products.exception.ResourceNotFoundException;
import com.substring.easybuy.products.repository.CategoryRepository;
import com.substring.easybuy.products.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setTitle(categoryRequestDto.getTitle());
        Category savedCategory = categoryRepository.save(category);
        return mapToResponseDto(savedCategory);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryResponseDto> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToResponseDto(category);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setTitle(categoryRequestDto.getTitle());
        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDto(updatedCategory);
    }

    @Override
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
