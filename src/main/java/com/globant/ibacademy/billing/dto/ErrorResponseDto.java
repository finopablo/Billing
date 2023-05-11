package com.globant.ibacademy.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponseDto {

    Integer status;
    List<String> errors = new ArrayList<>();
}
