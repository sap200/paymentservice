package com.mneepay.paymentservice.repository;
import com.mneepay.paymentservice.models.subscription.App;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionAppRepository  extends MongoRepository<App, String>  {
    List<App> findAllByMerchantId(String merchantId);
    Optional<App> findByAppKey(String appKey);
}
