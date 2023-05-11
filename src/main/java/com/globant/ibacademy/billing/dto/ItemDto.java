package com.globant.ibacademy.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDto {
    Integer id;
    ProductDto product;
    long qty;
    double price;
}
