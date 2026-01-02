package com.mneepay.paymentservice.dto;

import com.mneepay.paymentservice.models.CheckoutSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutSessionResponse {
    private String status;
    private String errorMessage;
    private CheckoutSession checkoutSession;
}
