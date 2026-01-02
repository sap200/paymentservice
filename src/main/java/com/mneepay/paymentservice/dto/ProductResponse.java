package com.mneepay.paymentservice.dto;

import com.mneepay.paymentservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String status;
    private String errorMessage;
    private Product product;
}
