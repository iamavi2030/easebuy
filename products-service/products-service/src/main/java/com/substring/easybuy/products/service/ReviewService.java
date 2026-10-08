package com.substring.easybuy.products.service;

import com.substring.easybuy.products.dtos.ReviewRequestDto;
import com.substring.easybuy.products.dtos.ReviewResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReviewService {

    ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto);

    List<ReviewResponseDto> getAllReviews();

    ReviewResponseDto getReviewById(Long id);

    ReviewResponseDto updateReview(Long id, ReviewRequestDto reviewRequestDto);

    void deleteReview(Long id);

    List<ReviewResponseDto> getReviewsByProduct(UUID productId);
}
