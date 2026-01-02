package com.mneepay.paymentservice.dto.subscription;

import com.mneepay.paymentservice.models.subscription.CustomerSubscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSubscriptionResponse {
    private String status;
    private String errorMessage;
    private CustomerSubscription customerSubscription;
}
