package com.substring.easybuy.products.repository;

import com.substring.easybuy.products.entity.Category;
import com.substring.easybuy.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

   @Query("SELECT c FROM Category c JOIN c.products p WHERE p.id = :productID")
   List<Category > findByProductID(@Param("productID")String productID);

}

