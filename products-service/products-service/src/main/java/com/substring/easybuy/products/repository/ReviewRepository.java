package com.substring.easybuy.products.repository;

import com.substring.easybuy.products.entity.Product;
import com.substring.easybuy.products.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product category);
}
