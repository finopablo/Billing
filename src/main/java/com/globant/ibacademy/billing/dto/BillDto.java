package com.globant.ibacademy.billing.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillDto {
    Integer id;
    @NotEmpty(message = "Bill Nuber is required")
    Integer number;

    LocalDateTime date;
    @NotEmpty(message = "Bill Customer is required")
    String customer;
    @NotEmpty(message = "As least one item is required")
    Set<ItemDto> items;
}
