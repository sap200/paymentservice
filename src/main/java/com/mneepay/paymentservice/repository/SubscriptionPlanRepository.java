package com.mneepay.paymentservice.repository;

import com.mneepay.paymentservice.models.subscription.Plan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends MongoRepository<Plan, String> {
    List<Plan> findAllByAppId(String appId);

}
