package com.mneepay.paymentservice.dto;

import com.mneepay.paymentservice.models.CheckoutItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutSessionRequest {
    private String merchantApiKey;
    private String name;
    private List<CheckoutItem> itemList;
    private String source;
    private String returnURL;
    private String webhookURL;
    @Builder.Default
    private String currencyISO="MNEE";
    @Builder.Default
    private int cartId = 0;
    @Builder.Default
    private double fees = 0.0d;
    @Builder.Default
    private double validationAmount = 0.0d;

}
