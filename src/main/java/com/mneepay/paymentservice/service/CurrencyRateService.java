package com.mneepay.paymentservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
public class CurrencyRateService {

    private static final String API_URL =
            "https://open.er-api.com/v6/latest/USD";

    private static final Duration CACHE_TTL = Duration.ofHours(8);

    private final RestTemplate restTemplate = new RestTemplate();

    // Cache
    private Map<String, Double> cachedRates;
    private Instant lastFetchTime;

    /**
     * Returns USD value for the given currency ISO
     * Example: EUR -> 0.85
     */
    public double getUSDValue(String currencyIso) {

        // Ensure cache is fresh
        if (shouldRefreshCache()) {
            fetchRatesFromApi();
        }

        currencyIso = currencyIso.toUpperCase();

        if (cachedRates == null || !cachedRates.containsKey(currencyIso)) {
            throw new IllegalArgumentException("Invalid currency ISO: " + currencyIso);
        }

        return cachedRates.get(currencyIso);
    }

    // ------------------ helpers ------------------

    private boolean shouldRefreshCache() {
        return cachedRates == null
                || lastFetchTime == null
                || Instant.now().isAfter(lastFetchTime.plus(CACHE_TTL));
    }

    private void fetchRatesFromApi() {

        Map<String, Object> response =
                restTemplate.getForObject(API_URL, Map.class);

        if (response == null || !"success".equals(response.get("result"))) {
            throw new RuntimeException("Failed to fetch currency rates");
        }

        Map<String, Object> rawRates = (Map<String, Object>) response.get("rates");

        // Convert safely
        this.cachedRates = rawRates.entrySet().stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                e -> ((Number) e.getValue()).doubleValue()
                        )
                );

        this.lastFetchTime = Instant.now();
    }

}
