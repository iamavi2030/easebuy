package com.substring.easybuy.products.controller;

import com.substring.easybuy.products.dtos.ReviewRequestDto;
import com.substring.easybuy.products.dtos.ReviewResponseDto;
import com.substring.easybuy.products.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        ReviewResponseDto createdReview = reviewService.createReview(reviewRequestDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        List<ReviewResponseDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Long id) {
        ReviewResponseDto review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        ReviewResponseDto updatedReview = reviewService.updateReview(id, reviewRequestDto);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProduct(@PathVariable UUID productId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }
}
