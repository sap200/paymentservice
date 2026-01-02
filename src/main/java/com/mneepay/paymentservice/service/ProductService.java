package com.mneepay.paymentservice.service;

import com.mneepay.paymentservice.constants.Constants;
import com.mneepay.paymentservice.dto.ProductRequest;
import com.mneepay.paymentservice.models.Product;
import com.mneepay.paymentservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(String merchantId, String productId, String name, String description, Double priceAmount, String imageURL) {
        Product product = Product.builder()
                .merchantId(merchantId)
                .productId(productId)
                .name(name)
                .description(description)
                .priceAmount(priceAmount)
                .imageURL(imageURL)
                .currency(Constants.MNEE_CURRENCY)
                .active(true)
                .build();

        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());

        return productRepository.save(product);
    }

    public List<Product> createMultipleProducts(List<ProductRequest> productList) {
        List<Product> products = new ArrayList<>();
        for(ProductRequest p : productList) {
            Product px = this.createProduct(p.getMerchantId(),
                    p.getProductId(),
                    p.getName(),
                    p.getDescription(),
                    p.getPriceAmount(),
                    p.getImageURL()
            );
            products.add(px);
        }

        return products;
    }

    public List<Product> getProductsByMerchant(String merchantId) {
        return productRepository.findAllByMerchantId(merchantId);
    }


    public Product deactivateProduct(String productId) throws Exception {
        productRepository.deleteById(productId);
        return null;
    }

    public Product getProductById(String productObjectId) throws Exception {
        Optional<Product> oProduct = productRepository.findById(productObjectId);
        if(oProduct.isEmpty())
            throw new Exception("product with id: " + productObjectId + " not found");

        return oProduct.get();
    }

    public Product updateProduct(String id, String productId, String name, String description, Double priceAmount, String imageURL) throws Exception {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new Exception("Product with id: " + id + " not found"));

        if(productId != null && !productId.isEmpty())
            product.setProductId(productId);

        if(name != null && !name.isEmpty())
            product.setName(name);

        if(description != null && !description.isEmpty())
            product.setDescription(description);

        if(priceAmount != null)
            product.setPriceAmount(priceAmount);

        if(imageURL != null && !imageURL.isEmpty())
            product.setImageURL(imageURL);

        product.setUpdatedAt(Instant.now());

        return productRepository.save(product);
    }
}
