package com.mneepay.paymentservice.repository;

import com.mneepay.paymentservice.models.CheckoutSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CheckoutSessionRepository extends MongoRepository<CheckoutSession, String> {
    List<CheckoutSession> findAllByMerchantId(String merchantId);
}
