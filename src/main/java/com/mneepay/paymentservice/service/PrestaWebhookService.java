package com.mneepay.paymentservice.service;

import com.mneepay.paymentservice.models.CheckoutSession;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PrestaWebhookService {
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Sends a webhook to PrestaShop with checkout session data
     *
     * @param checkoutSession  checkoutsession object
     * @return response body from PrestaShop
     */
    public String sendWebhook(CheckoutSession checkoutSession) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("cart_id", checkoutSession.getCartId());
        payload.put("status", checkoutSession.getStatus());
        payload.put("amount", checkoutSession.getPrestaValidationAmount());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        // 1️⃣ Print the URL for debugging
        System.out.println("WEBHOOK URL: " + checkoutSession.getPrestaWebhookURL());

        // 2️⃣ Use URI.create() to preserve query params exactly
        URI uri = URI.create(checkoutSession.getPrestaWebhookURL().trim());

        System.out.println("Final URI: " + uri.toString());

        // 3️⃣ Send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        return response.getBody();
    }

}