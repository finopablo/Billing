package com.globant.ibacademy.billing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    Integer id;
    @NotBlank(message = "Product should have a name")
    String name;
    String description;
    @NotNull(message ="Product should have a price")
    double price;
}
