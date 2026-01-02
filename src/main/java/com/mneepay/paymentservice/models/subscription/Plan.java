package com.mneepay.paymentservice.models.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("subscription_plans_collection")
public class Plan {
    @Id
    private String id;
    private String appId;
    private String name;
    private String code;
    private double amount;
    private String currency;
    // monthly | yearly
    private String interval;
    // active | archived
    private String status;
    // Entitlements / features
    private List<String> features;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
