package com.mneepay.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String merchantId;
    private String productId;
    private String name;
    private String description;
    private String imageURL;
    private Double priceAmount; // In MNEE
}
