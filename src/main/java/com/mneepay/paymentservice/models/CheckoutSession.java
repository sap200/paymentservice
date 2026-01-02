package com.mneepay.paymentservice.models;

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
@Document("checkout_session_collection")
public class CheckoutSession {
    @Id
    private String id;
    private String merchantId;
    private String name;
    private List<CheckoutItem> itemList;
    private String currency;
    private Double totalAmount;
    private String hostedPage;
    private String status;
    private String source;
    private String successURL;
    private String failureURL;
    private String cancelURL;
    private String pendingURL;
    private String prestaReturnURL;
    private String prestaWebhookURL;
    private int cartId;
    private double fees;
    private double prestaValidationAmount;
    private String merchantWalletAddress;
    private Map<String, String> detailsMap;
    private String ticketId;
    private String transactionId;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
