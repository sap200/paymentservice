package com.mneepay.paymentservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("product_collection")
public class Product {
    @Id
    private String id;
    @Indexed(unique = true)
    private String productId;
    private String merchantId;
    private String name;
    private String description;
    private Double priceAmount; // In MNEE
    private String currency; // "MNEE"
    private String imageURL;
    private boolean active;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
