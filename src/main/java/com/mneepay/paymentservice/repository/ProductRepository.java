package com.mneepay.paymentservice.repository;

import com.mneepay.paymentservice.models.Merchant;
import com.mneepay.paymentservice.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findAllByMerchantId(String merchantId); // find all product corresponding to a merchant
    Optional<Product> findByProductId(String productId); // find product by product Id

}
