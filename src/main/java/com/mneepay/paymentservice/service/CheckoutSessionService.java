package com.mneepay.paymentservice.service;

import com.mneepay.paymentservice.constants.Constants;
import com.mneepay.paymentservice.models.CheckoutItem;
import com.mneepay.paymentservice.models.CheckoutSession;
import com.mneepay.paymentservice.models.Merchant;
import com.mneepay.paymentservice.models.Product;
import com.mneepay.paymentservice.repository.CheckoutSessionRepository;
import com.mneepay.paymentservice.repository.MerchantRepository;
import com.mneepay.paymentservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;

@Service
public class CheckoutSessionService {
    @Autowired
    private CheckoutSessionRepository checkoutSessionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private PrestaWebhookService prestaWebhookService;

    @Autowired
    private CurrencyRateService currencyRateService;

    @Value("${mnee.hosted.page.base}")
    private String hostedPageBase;

    public CheckoutSession createCheckoutSession(String merchantApiKey, List<CheckoutItem> checkoutItems, String name, String source, String prestaReturnURL, String prestaWebhookURL, int cartId, double fees, double prestaValidationAmount, String prestaCurrencyISO) throws Exception {
        System.out.println(name);
        Merchant merchant = merchantRepository.findByApiKey(merchantApiKey).orElseThrow(() -> new Exception("merchant api-key : " + merchantApiKey + " not found."));
        double totalAmount = 0d;
        for (CheckoutItem checkoutItem : checkoutItems) {
                String productId = checkoutItem.getProductId();
                Optional<Product> product = productRepository.findByProductId(productId);
                if (product.isPresent()) {
                    if (product.get().isActive() && product.get().getMerchantId().equals(merchant.getId())) {
                        checkoutItem.setUnitPrice(product.get().getPriceAmount());
                        checkoutItem.setStatus(Constants.PRODUCT_AVAILABLE);
                        checkoutItem.setProductName(product.get().getName());
                        checkoutItem.setProductObjectId(product.get().getId());
                        totalAmount += checkoutItem.getUnitPrice() * checkoutItem.getQuantity();
                    } else {
                        checkoutItem.setStatus(Constants.PRODUCT_NOT_AVAILABLE);
                        checkoutItem.setUnitPrice(0d);
                    }

                } else {
                    checkoutItem.setStatus(Constants.INVALID_PRODUCT);
                }
        }

        totalAmount += fees;

        if(source.equals(Constants.PRESTASHOP_SOURCE_NAME)) {
            System.out.println("currency: " + prestaCurrencyISO);
            totalAmount = currencyRateService.getUSDValue(prestaCurrencyISO) * prestaValidationAmount;

        }



        CheckoutSession checkoutSession = CheckoutSession.builder()
                .merchantId(merchant.getId())
                .successURL(merchant.getSuccessWebhookURL())
                .failureURL(merchant.getFailureWebhookURL())
                .cancelURL(merchant.getCancelWebhookURL())
                .pendingURL(merchant.getPendingWebhookURL())
                .prestaWebhookURL(prestaWebhookURL)
                .prestaReturnURL(prestaReturnURL)
                .source(source)
                .name(name)
                .cartId(cartId)
                .prestaValidationAmount(prestaValidationAmount)
                .fees(fees)
                .merchantWalletAddress(merchant.getWalletAddress())
                .itemList(checkoutItems)
                .status(Constants.CHECKOUT_STATUS_CREATED)
                .currency(Constants.MNEE_CURRENCY)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .totalAmount(roundUpFiveDecimalsBD(totalAmount))
                .build();

        CheckoutSession savedObject = checkoutSessionRepository.save(checkoutSession);
        String link = hostedPageBase + "/" + savedObject.getId();
        savedObject.setHostedPage(link);

        return checkoutSessionRepository.save(savedObject);
    }

    private static double roundUpFiveDecimalsBD(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(5, RoundingMode.CEILING);
        return bd.doubleValue();
    }

    public CheckoutSession getCheckoutSessionById(String checkoutSessionObjectId) throws Exception {
        return checkoutSessionRepository.findById(checkoutSessionObjectId).orElseThrow(() -> new Exception("unable to find checkout session with id: " + checkoutSessionObjectId));
    }

    public List<CheckoutSession> getAllCheckoutSessionByMerchantId(String merchantId) {
       return checkoutSessionRepository.findAllByMerchantId(merchantId);
    }

    public CheckoutSession updateDetailsMap(String checkoutSessionObjectId, String ticketId, Map<String, String> map) throws Exception {
        CheckoutSession checkoutSession = checkoutSessionRepository.findById(checkoutSessionObjectId).orElseThrow(() -> new Exception("unable to find checkout session with id: " + checkoutSessionObjectId));
        checkoutSession.setDetailsMap(map);
        if(checkoutSession.getDetailsMap().get("status").equals("SUCCESS") || checkoutSession.getDetailsMap().get("status").equals("MINED"))
            checkoutSession.setStatus(Constants.CHECKOUT_STATUS_PAID);
        else if (checkoutSession.getDetailsMap().get("status").equals("FAILED"))
            checkoutSession.setStatus(Constants.CHECKOUT_STATUS_FAILED);
        else if (checkoutSession.getDetailsMap().get("status").equals("BROADCASTING"))
            checkoutSession.setStatus(Constants.CHECKOUT_STATUS_PENDING);

        checkoutSession.setTicketId(ticketId);
        checkoutSession.setUpdatedAt(Instant.now());
        CheckoutSession newCheckoutSession =  checkoutSessionRepository.save(checkoutSession);

        // send webhook
        if(newCheckoutSession.getSource().equals(Constants.PRESTASHOP_SOURCE_NAME)) {
            prestaWebhookService.sendWebhook(newCheckoutSession);
        }

        return newCheckoutSession;
    }

    // update checkout status
    // create a payment_collection where we actually store on chain payment details.
    public CheckoutSession cancelCheckoutSession(String checkoutSessionObjectId, String reason) throws Exception {
        CheckoutSession checkoutSession = checkoutSessionRepository.findById(checkoutSessionObjectId).orElseThrow(() -> new Exception("unable to find checkout session with id: " + checkoutSessionObjectId));
        if(checkoutSession.getStatus().equals("CREATED")) {
            checkoutSession.setStatus(Constants.CHECKOUT_STATUS_CANCELLED);
            System.out.println(reason);
            Map<String, String> map = new HashMap<>();
            map.put("reason", reason);
            checkoutSession.setDetailsMap(map);
            checkoutSession.setUpdatedAt(Instant.now());
            CheckoutSession newCheckoutSession =  checkoutSessionRepository.save(checkoutSession);
            // send webhook
            if(newCheckoutSession.getSource().equals(Constants.PRESTASHOP_SOURCE_NAME)) {
                prestaWebhookService.sendWebhook(newCheckoutSession);
            }
            return newCheckoutSession;
        } else {
            throw new Exception("checkout cannot be cancelled as it is in " + checkoutSession.getStatus() + " state");
        }
    }
}
