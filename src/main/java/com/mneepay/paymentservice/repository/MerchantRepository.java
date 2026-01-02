package com.mneepay.paymentservice.repository;

import com.mneepay.paymentservice.models.Merchant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MerchantRepository extends MongoRepository<Merchant, String> {
    // Find merchant by email (for login or validation)
    Optional<Merchant> findByEmail(String email);
    Optional<Merchant> findByApiKey(String apiKey);

    // Check if email already exists
    boolean existsByEmail(String email);
}

