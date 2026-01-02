package com.mneepay.paymentservice.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanRequest {
    private String appId;
    private String name;
    private String code;
    private String status;
    private double amount;
    // monthly | yearly
    private String interval;
    // Entitlements / features
    private List<String> features;
}
