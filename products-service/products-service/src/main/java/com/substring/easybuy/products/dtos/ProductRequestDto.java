package com.substring.easybuy.products.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    @NotBlank(message = "Product title is required")
    private String title;
    
    @NotBlank(message = "Short description is required")
    private String shortDesc;
    
    private String longDesc;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;
    
    @Min(value = 0, message = "Discount cannot be negative")
    private Integer discount;
    
    private Boolean live;
    
    private List<String> productImages;
    
    private List<Long> categoryIds;
}
