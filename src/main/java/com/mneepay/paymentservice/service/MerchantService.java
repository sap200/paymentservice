package com.mneepay.paymentservice.service;

import cloud.pangeacyber.pangea.authn.responses.ClientSessionLogoutResponse;
import cloud.pangeacyber.pangea.authn.responses.ClientUserinfoResponse;
import com.mneepay.paymentservice.models.Merchant;
import com.mneepay.paymentservice.repository.MerchantRepository;
import com.mongodb.client.ClientSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired PangeaService pangeaService;

    public Merchant createMerchant(String businessName, String email, String walletAddress, String successWebhookURL, String failureWebhookURL, String cancelWebhookURL, String pendingWebhookURL) throws Exception{

        if(merchantRepository.existsByEmail(email))
            throw new Exception("email already exists");

        UUID uuid = UUID.randomUUID();
        String apiKey = uuid.toString();
        Merchant merchant = Merchant.builder()
                .businessName(businessName)
                .email(email)
                .walletAddress(walletAddress)
                .apiKey(apiKey)
                .cancelWebhookURL(cancelWebhookURL)
                .successWebhookURL(successWebhookURL)
                .failureWebhookURL(failureWebhookURL)
                .pendingWebhookURL(pendingWebhookURL)
                .build();

        merchant.setCreatedAt(Instant.now());
        merchant.setUpdatedAt(Instant.now());

        return merchantRepository.save(merchant);
    }

    public Merchant getMerchantByEmail(String email) throws Exception {
       Optional<Merchant> merchant =  merchantRepository.findByEmail(email);
       if(merchant.isEmpty())
           throw new Exception("Merchant with email: " + email + " doesn't exists.");

       return merchant.get();
    }

    // Update webhook URL
    public Merchant updateSuccessWebhook(String merchantId, String successWebhookUrl) throws Exception {
        Optional<Merchant> merchant = merchantRepository.findById(merchantId);
        if(merchant.isEmpty())
            throw new Exception("Merchant with id: " + merchantId + " doesn't exists.");


        merchant.get().setSuccessWebhookURL(successWebhookUrl);
        merchant.get().setUpdatedAt(Instant.now());
        return merchantRepository.save(merchant.get());
    }

    public Merchant updateFailureWebhook(String merchantId, String failureWebhookUrl) throws Exception {
        Optional<Merchant> merchant = merchantRepository.findById(merchantId);
        if(merchant.isEmpty())
            throw new Exception("Merchant with id: " + merchantId + " doesn't exists.");


        merchant.get().setFailureWebhookURL(failureWebhookUrl);
        merchant.get().setUpdatedAt(Instant.now());
        return merchantRepository.save(merchant.get());
    }

    public Merchant updateCancelWebhook(String merchantId, String cancelWebhook) throws Exception {
        Optional<Merchant> merchant = merchantRepository.findById(merchantId);
        if(merchant.isEmpty())
            throw new Exception("Merchant with id: " + merchantId + " doesn't exists.");


        merchant.get().setCancelWebhookURL(cancelWebhook);
        merchant.get().setUpdatedAt(Instant.now());
        return merchantRepository.save(merchant.get());
    }

    public Merchant updatePendingWebhook(String merchantId, String pendingWebhook) throws Exception {
        Optional<Merchant> merchant = merchantRepository.findById(merchantId);
        if(merchant.isEmpty())
            throw new Exception("Merchant with id: " + merchantId + " doesn't exists.");


        merchant.get().setPendingWebhookURL(pendingWebhook);
        merchant.get().setUpdatedAt(Instant.now());
        return merchantRepository.save(merchant.get());
    }
    // Regenerate API key
    public Merchant regenerateApiKey(String merchantId) throws Exception {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new Exception("Merchant with id: " + merchantId + " not found"));
        merchant.setApiKey(UUID.randomUUID().toString());
        merchant.setUpdatedAt(Instant.now());
        return merchantRepository.save(merchant);
    }

    public Merchant changeWalletAddress(String merchantId, String walletAddress) throws Exception {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new Exception("Merchant with id: " + merchantId + " not found"));
        merchant.setWalletAddress(walletAddress);
        merchant.setUpdatedAt(Instant.now());
        return merchantRepository.save(merchant);
    }

    public Merchant getMerchantByCode(String code) throws Exception {
        ClientUserinfoResponse clientUserinfoResponse = pangeaService.getClientUserInfo(code);
        String email = clientUserinfoResponse.getResult().getActiveToken().getEmail();
        String token = clientUserinfoResponse.getResult().getActiveToken().getToken();


        Optional<Merchant> merchant = merchantRepository.findByEmail(email);
        if(merchant.isPresent()) {
            Merchant m = merchant.get();
            m.setTemporaryClientToken(token);
            return m;
        } else {
            String businessName = clientUserinfoResponse.getResult().getActiveToken().getProfile().get("buisness_name");
            String cancelWebhookURL = clientUserinfoResponse.getResult().getActiveToken().getProfile().get("cancel_webhook_url");
            String failureWebhookURL = clientUserinfoResponse.getResult().getActiveToken().getProfile().get("failure_webhook_url");
            String pendingWebhookURL = clientUserinfoResponse.getResult().getActiveToken().getProfile().get("pending_webhook_url");
            String successWebhookURL = clientUserinfoResponse.getResult().getActiveToken().getProfile().get("success_webhook_url");
            String walletAddress = clientUserinfoResponse.getResult().getActiveToken().getProfile().get("mnee_wallet_address");

            Merchant m = this.createMerchant(businessName, email, walletAddress, successWebhookURL, failureWebhookURL, cancelWebhookURL, pendingWebhookURL);
            m.setTemporaryClientToken(token);
            return m;
        }
    }

    public boolean logout(String token) throws Exception {
        ClientSessionLogoutResponse clientSessionLogoutResponse = pangeaService.sessionLogout(token);
        System.out.println(clientSessionLogoutResponse.getSummary() + ", " + clientSessionLogoutResponse.getStatus());
        return clientSessionLogoutResponse.getStatus().equals("Success");
    }


}
