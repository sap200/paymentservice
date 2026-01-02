package com.mneepay.paymentservice.models;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("merchant_collection")
public class Merchant {
    @Id
    private String id;
    private String businessName;
    private String email;
    private String walletAddress;
    private String successWebhookURL;
    private String failureWebhookURL;
    private String cancelWebhookURL;
    private String pendingWebhookURL;
    private String apiKey;
    private String temporaryClientToken;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
