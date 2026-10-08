package com.substring.easybuy.products.service.impl;

import com.substring.easybuy.products.dtos.ReviewRequestDto;
import com.substring.easybuy.products.dtos.ReviewResponseDto;
import com.substring.easybuy.products.entity.Product;
import com.substring.easybuy.products.entity.Review;
import com.substring.easybuy.products.exception.ResourceNotFoundException;
import com.substring.easybuy.products.repository.ProductRepository;
import com.substring.easybuy.products.repository.ReviewRepository;
import com.substring.easybuy.products.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto) {
        Product product = productRepository.findById(reviewRequestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + reviewRequestDto.getProductId()));
        
        Review review = new Review();
        review.setTitle(reviewRequestDto.getTitle());
        review.setComment(reviewRequestDto.getComment());
        review.setRating(reviewRequestDto.getRating());
        review.setProduct(product);
        
        Review savedReview = reviewRepository.save(review);
        return mapToResponseDto(savedReview);
    }

    @Override
    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponseDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        return mapToResponseDto(review);
    }

    @Override
    @Transactional
    public ReviewResponseDto updateReview(Long id, ReviewRequestDto reviewRequestDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        
        Product product = productRepository.findById(reviewRequestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + reviewRequestDto.getProductId()));
        
        review.setTitle(reviewRequestDto.getTitle());
        review.setComment(reviewRequestDto.getComment());
        review.setRating(reviewRequestDto.getRating());
        review.setProduct(product);
        
        Review updatedReview = reviewRepository.save(review);
        return mapToResponseDto(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewResponseDto> getReviewsByProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return reviewRepository.findByProduct(product).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private ReviewResponseDto mapToResponseDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setTitle(review.getTitle());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setProductId(review.getProduct() != null ? review.getProduct().getId() : null);
        return dto;
    }
}
