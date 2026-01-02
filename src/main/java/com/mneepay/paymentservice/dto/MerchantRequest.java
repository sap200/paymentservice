package com.mneepay.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantRequest {
    private String businessName;
    private String email;
    private String walletAddress;
    private String successWebhookURL;
    private String failureWebhookURL;
    private String cancelWebhookURL;
    private String pendingWebhookURL;

}
