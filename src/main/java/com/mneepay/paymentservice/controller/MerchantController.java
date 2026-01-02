package com.mneepay.paymentservice.controller;

import com.mneepay.paymentservice.dto.MerchantRequest;
import com.mneepay.paymentservice.dto.MerchantResponse;
import com.mneepay.paymentservice.models.Merchant;
import com.mneepay.paymentservice.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mneepay.paymentservice.constants.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    // register a new merchant.
    @PostMapping("/register")
    public ResponseEntity<MerchantResponse> registerMerchant(@RequestBody MerchantRequest request) {
        try {
            Merchant merchant = merchantService.createMerchant(
                    request.getBusinessName(),
                    request.getEmail(),
                    request.getWalletAddress(),
                    request.getSuccessWebhookURL(),
                    request.getFailureWebhookURL(),
                    request.getCancelWebhookURL(),
                    request.getPendingWebhookURL()
            );
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .merchant(merchant)
                    .status(Constants.SUCCESS)
                    .build();

            return ResponseEntity.ok(createMerchantResponse);
        } catch(Exception ex) {
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .merchant(null)
                    .build();

            return ResponseEntity.badRequest().body(createMerchantResponse);
        }
    }

    @GetMapping("/get-by-code/{code}")
    public ResponseEntity<MerchantResponse> getMerchantByCode(@PathVariable String code) {
        try {
            Merchant merchant = merchantService.getMerchantByCode(code);

            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .merchant(merchant)
                    .status(Constants.SUCCESS)
                    .build();

            return ResponseEntity.ok(createMerchantResponse);
        } catch(Exception ex) {
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .merchant(null)
                    .build();

            return ResponseEntity.badRequest().body(createMerchantResponse);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<MerchantResponse> getMerchant(@PathVariable String email) {
        try {
            Merchant merchant = merchantService.getMerchantByEmail(email);

            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .merchant(merchant)
                    .status(Constants.SUCCESS)
                    .build();

            return ResponseEntity.ok(createMerchantResponse);
        } catch(Exception ex) {
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .merchant(null)
                    .build();

            return ResponseEntity.badRequest().body(createMerchantResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Boolean>> doLogout(@RequestBody Map<String, String> map) {
        try {
            boolean status = merchantService.logout(map.get("token"));
            Map<String, Boolean> rmap = new HashMap<>();
            rmap.put("status", status);
            System.out.println("Logout status" + map.get("token") + ":" + status);
            if(status) {
                return ResponseEntity.ok(rmap);
            } else {
                return ResponseEntity.badRequest().body(rmap);
            }
        } catch(Exception ex) {
            Map<String, Boolean> rmap = new HashMap<>();
            rmap.put("status", false);
            System.out.println("Logout Exception: " + ex.getMessage());
            return ResponseEntity.badRequest().body(rmap);
        }
    }


    @PostMapping("/{merchantId}/update-success-webhook")
    public ResponseEntity<MerchantResponse> updateSuccessWebhook(@PathVariable String merchantId,
                                                          @RequestBody MerchantRequest updateMerchantRequest) {
        try {
            Merchant merchant = merchantService.updateSuccessWebhook(merchantId, updateMerchantRequest.getSuccessWebhookURL());
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .merchant(merchant)
                    .status(Constants.SUCCESS)
                    .build();

            return ResponseEntity.ok(createMerchantResponse);
        } catch(Exception ex) {
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .merchant(null)
                    .build();

            return ResponseEntity.badRequest().body(createMerchantResponse);
        }
    }

    @PostMapping("/{merchantId}/update-failure-webhook")
    public ResponseEntity<MerchantResponse> updateFailureWebhook(@PathVariable String merchantId,
                                                                 @RequestBody MerchantRequest updateMerchantRequest) {
        try {
            Merchant merchant = merchantService.updateFailureWebhook(merchantId, updateMerchantRequest.getFailureWebhookURL());
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .merchant(merchant)
                    .status(Constants.SUCCESS)
                    .build();

            return ResponseEntity.ok(createMerchantResponse);
        } catch(Exception ex) {
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .merchant(null)
                    .build();

            return ResponseEntity.badRequest().body(createMerchantResponse);
        }
    }

    @PostMapping("/{merchantId}/update-cancel-webhook")
    public ResponseEntity<MerchantResponse> updateCancelWebhook(@PathVariable String merchantId,
                                                                 @RequestBody MerchantRequest updateMerchantRequest) {
        try {
            Merchant merchant = merchantService.updateCancelWebhook(merchantId, updateMerchantRequest.getCancelWebhookURL());
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .merchant(merchant)
                    .status(Constants.SUCCESS)
                    .build();

            return ResponseEntity.ok(createMerchantResponse);
        } catch(Exception ex) {
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .merchant(null)
                    .build();

            return ResponseEntity.badRequest().body(createMerchantResponse);
        }
    }

    @PostMapping("/{merchantId}/update-pending-webhook")
    public ResponseEntity<MerchantResponse> updatePendingWebhook(@PathVariable String merchantId,
                                                                @RequestBody MerchantRequest updateMerchantRequest) {
        try {
            Merchant merchant = merchantService.updatePendingWebhook(merchantId, updateMerchantRequest.getPendingWebhookURL());
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .merchant(merchant)
                    .status(Constants.SUCCESS)
                    .build();

            return ResponseEntity.ok(createMerchantResponse);
        } catch(Exception ex) {
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .merchant(null)
                    .build();

            return ResponseEntity.badRequest().body(createMerchantResponse);
        }
    }

    @PostMapping("/{merchantId}/regenerate-api-key")
    public ResponseEntity<MerchantResponse> regenerateMerchantAPIKey(@PathVariable String merchantId) {
        try {
            Merchant merchant = merchantService.regenerateApiKey(merchantId);
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .merchant(merchant)
                    .status(Constants.SUCCESS)
                    .build();

            return ResponseEntity.ok(createMerchantResponse);
        } catch(Exception ex) {
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .merchant(null)
                    .build();

            return ResponseEntity.badRequest().body(createMerchantResponse);
        }
    }

    @PostMapping("/{merchantId}/update-wallet-address")
    public ResponseEntity<MerchantResponse> updateWalletAddress(@PathVariable String merchantId,
                                                          @RequestBody MerchantRequest updateMerchantRequest) {
        try {
            Merchant merchant = merchantService.changeWalletAddress(merchantId, updateMerchantRequest.getWalletAddress());
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .merchant(merchant)
                    .status(Constants.SUCCESS)
                    .build();

            return ResponseEntity.ok(createMerchantResponse);
        } catch(Exception ex) {
            MerchantResponse createMerchantResponse = MerchantResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .merchant(null)
                    .build();

            return ResponseEntity.badRequest().body(createMerchantResponse);
        }
    }



}
