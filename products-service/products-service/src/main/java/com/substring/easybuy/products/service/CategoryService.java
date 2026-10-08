package com.substring.easybuy.products.service;

import com.substring.easybuy.products.dtos.CategoryRequestDto;
import com.substring.easybuy.products.dtos.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    List<CategoryResponseDto> getAllCategories();

    Page<CategoryResponseDto> getAllCategories(Pageable pageable);

    CategoryResponseDto getCategoryById(Long id);

    CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto);

    void deleteCategory(Long id);
}
