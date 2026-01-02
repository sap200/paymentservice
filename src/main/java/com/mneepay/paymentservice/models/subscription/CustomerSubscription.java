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
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("subscription_customer_collection")

public class CustomerSubscription {

    @Id
    private String id;
    private String appId;
    private String appKey;
    private String planId;
    private String customerId;
    // active | expired | canceled | archived
    private String status;
    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;
    private String renewalMode;
    private String merchantWalletAddress;
    private String hostedSubscriptionPage;
    private List<Map<String, String>> detailsMapList;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
