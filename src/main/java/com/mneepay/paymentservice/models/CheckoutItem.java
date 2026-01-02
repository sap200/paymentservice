package com.mneepay.paymentservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItem {
    private String productObjectId;
    private String productId;
    private String productName;
    private Long quantity;
    private Double unitPrice;
    private String status;
}
