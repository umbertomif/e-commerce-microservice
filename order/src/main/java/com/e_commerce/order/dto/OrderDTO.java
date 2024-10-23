package com.e_commerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotBlank(message = "Product ID cannot be blank")
    private String productId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    @NotBlank(message = "Customer ID cannot be blank")
    private String customerId;
}