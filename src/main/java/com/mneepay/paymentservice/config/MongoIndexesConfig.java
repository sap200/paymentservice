package com.mneepay.paymentservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

import jakarta.annotation.PostConstruct;

@Configuration
public class MongoIndexesConfig {

    private final MongoTemplate mongoTemplate;

    public MongoIndexesConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void createIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps("product_collection");

        Index index = new Index()
                .on("productId", Sort.Direction.ASC)
                .unique();

        indexOps.createIndex(index); // ✅ Not deprecated
        System.out.println("✅ Unique index on productId created/ensured");
    }
}
