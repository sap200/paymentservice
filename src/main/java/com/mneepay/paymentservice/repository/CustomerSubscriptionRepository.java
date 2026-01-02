package com.mneepay.paymentservice.repository;

import com.mneepay.paymentservice.models.subscription.CustomerSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerSubscriptionRepository extends MongoRepository<CustomerSubscription, String> {
    boolean existsByAppIdAndCustomerIdAndStatusIn(
            String appId,
            String customerId,
            List<String> statuses
    );

    Optional<CustomerSubscription> findByAppIdAndCustomerIdAndStatus(
            String appId,
            String customerId,
            String status
    );

    List<CustomerSubscription> findAllByAppKey(String appId);
}
