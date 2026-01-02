package com.mneepay.paymentservice.dto;

import com.mneepay.paymentservice.models.Merchant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantResponse {
    private String status;
    private String errorMessage;
    private Merchant merchant;

}
