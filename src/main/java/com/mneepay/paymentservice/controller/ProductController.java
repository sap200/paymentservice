package com.mneepay.paymentservice.controller;

import com.mneepay.paymentservice.constants.Constants;
import com.mneepay.paymentservice.dto.ProductRequest;
import com.mneepay.paymentservice.dto.ProductResponse;
import com.mneepay.paymentservice.models.Product;
import com.mneepay.paymentservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
            Product product = productService.createProduct(
                    request.getMerchantId(),
                    request.getProductId(),
                    request.getName(),
                    request.getDescription(),
                    request.getPriceAmount(),
                    request.getImageURL()
            );
            ProductResponse productResponse = ProductResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .product(product)
                    .build();

            return ResponseEntity.ok(productResponse);
    }

    @PostMapping("/create-all")
    public ResponseEntity<List<Product>> createProduct(@RequestBody List<ProductRequest> request) {
        List<Product> products = productService.createMultipleProducts(request);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{merchantId}/get-all-product-by-merchantid")
    public ResponseEntity<List<Product>> getAllProductByMerchantId(@PathVariable String merchantId) {
        List<Product> list = productService.getProductsByMerchant(merchantId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{productId}/delete-product")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable String productId) {
        try {
            Product product = productService.deactivateProduct(productId);
            ProductResponse productResponse = ProductResponse.builder()
                    .status(Constants.SUCCESS)
                    .product(product)
                    .build();

            return ResponseEntity.ok(productResponse);
        } catch(Exception ex) {
            ProductResponse productResponse = ProductResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(productResponse);
        }
    }

    @GetMapping("/{id}/get-product")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
        try {
            Product product = productService.getProductById(id);

            ProductResponse productResponse = ProductResponse.builder()
                    .status(Constants.SUCCESS)
                    .product(product)
                    .build();

            return ResponseEntity.ok(productResponse);

        } catch(Exception ex) {
            ProductResponse productResponse = ProductResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(productResponse);
        }
    }

    @PostMapping("/{id}/update-product")
    public ResponseEntity<ProductResponse> deactivateProduct(@PathVariable String id, @RequestBody ProductRequest productRequest) {
        try {
            Product product = productService.updateProduct(id, productRequest.getProductId(),
                    productRequest.getName(),
                    productRequest.getDescription(),
                    productRequest.getPriceAmount(),
                    productRequest.getImageURL());

            ProductResponse productResponse = ProductResponse.builder()
                    .status(Constants.SUCCESS)
                    .product(product)
                    .build();

            return ResponseEntity.ok(productResponse);
        } catch(Exception ex) {
            ProductResponse productResponse = ProductResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(productResponse);
        }
    }
}
