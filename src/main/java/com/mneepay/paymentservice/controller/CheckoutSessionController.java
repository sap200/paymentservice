package com.mneepay.paymentservice.controller;

import com.mneepay.paymentservice.constants.Constants;
import com.mneepay.paymentservice.dto.CheckoutCancelRequest;
import com.mneepay.paymentservice.dto.CheckoutSessionRequest;
import com.mneepay.paymentservice.dto.CheckoutSessionResponse;
import com.mneepay.paymentservice.dto.UpdateDetailsMapRequest;
import com.mneepay.paymentservice.models.CheckoutSession;
import com.mneepay.paymentservice.service.CheckoutSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkout-session")
public class CheckoutSessionController {

    @Autowired
    private CheckoutSessionService checkoutSessionService;

    @PostMapping("/create")
    public ResponseEntity<CheckoutSessionResponse> createProduct(@RequestBody CheckoutSessionRequest request) {
        try {
            CheckoutSession checkoutSession = checkoutSessionService.createCheckoutSession(request.getMerchantApiKey(), request.getItemList(), request.getName(), request.getSource(), request.getReturnURL(), request.getWebhookURL(), request.getCartId(), request.getFees(), request.getValidationAmount(), request.getCurrencyISO());
            CheckoutSessionResponse checkoutSessionResponse = CheckoutSessionResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .checkoutSession(checkoutSession)
                    .build();

            return ResponseEntity.ok(checkoutSessionResponse);
        } catch(Exception ex) {
            ex.printStackTrace();
            CheckoutSessionResponse checkoutSessionResponse = CheckoutSessionResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(checkoutSessionResponse);
        }
    }

    @GetMapping("/{checkoutSessionObjectId}/get")
    public ResponseEntity<CheckoutSessionResponse> getCheckoutSessionById(@PathVariable String checkoutSessionObjectId) {
        try {

            CheckoutSession checkoutSession = checkoutSessionService.getCheckoutSessionById(checkoutSessionObjectId);
            CheckoutSessionResponse checkoutSessionResponse = CheckoutSessionResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .checkoutSession(checkoutSession)
                    .build();

            return ResponseEntity.ok(checkoutSessionResponse);

        } catch (Exception ex) {
            CheckoutSessionResponse checkoutSessionResponse = CheckoutSessionResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(checkoutSessionResponse);
        }
    }

    @GetMapping("/{merchantId}/get-all-checkout-session-by-merchant-id")
    public ResponseEntity<List<CheckoutSession>> getAllCheckoutSessionByMerchantId(@PathVariable String merchantId) {
        List<CheckoutSession> checkoutSessionList = checkoutSessionService.getAllCheckoutSessionByMerchantId(merchantId);
        return ResponseEntity.ok(checkoutSessionList);
    }

    @PostMapping("{checkoutSessionObjectId}/update-details-map")
    public ResponseEntity<CheckoutSessionResponse> updateDetailsMap(@PathVariable String checkoutSessionObjectId, @RequestBody UpdateDetailsMapRequest request) {
        try {
            CheckoutSession checkoutSession = checkoutSessionService.updateDetailsMap(checkoutSessionObjectId, request.getTicketId(), request.getDetailsMap());
            CheckoutSessionResponse checkoutSessionResponse = CheckoutSessionResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .checkoutSession(checkoutSession)
                    .build();

            return ResponseEntity.ok(checkoutSessionResponse);
        } catch(Exception ex) {
            ex.printStackTrace();
            CheckoutSessionResponse checkoutSessionResponse = CheckoutSessionResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(checkoutSessionResponse);
        }
    }

    @PostMapping("{checkoutSessionObjectId}/cancel-checkout")
    public ResponseEntity<CheckoutSessionResponse> cancelCheckout(@PathVariable String checkoutSessionObjectId, @RequestBody CheckoutCancelRequest request) {
        try {
            CheckoutSession checkoutSession = checkoutSessionService.cancelCheckoutSession(checkoutSessionObjectId, request.getReason());
            CheckoutSessionResponse checkoutSessionResponse = CheckoutSessionResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .checkoutSession(checkoutSession)
                    .build();

            return ResponseEntity.ok(checkoutSessionResponse);
        } catch(Exception ex) {
            ex.printStackTrace();
            CheckoutSessionResponse checkoutSessionResponse = CheckoutSessionResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(checkoutSessionResponse);
        }
    }
}
