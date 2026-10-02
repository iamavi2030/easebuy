package com.substring.easybuy.products.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private String title;
    private String shortDesc;
    private String longDesc;
    private Double price;
    private Integer discount;
    private Boolean live;
    private List<String> productImages;
    private List<CategoryResponseDto> categories;
    private List<ReviewResponseDto> reviews;
}
