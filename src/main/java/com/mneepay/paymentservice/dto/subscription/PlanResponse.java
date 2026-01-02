package com.mneepay.paymentservice.dto.subscription;

import com.mneepay.paymentservice.models.subscription.App;
import com.mneepay.paymentservice.models.subscription.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {
    private String status;
    private String errorMessage;
    private Plan plan;
}
