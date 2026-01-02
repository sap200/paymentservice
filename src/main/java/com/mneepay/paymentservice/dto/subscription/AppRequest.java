package com.mneepay.paymentservice.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppRequest {
    private String merchantId;
    private String name;
    private String description;
    private String webhookURL; // should accept post request.
}
