package com.mneepay.paymentservice.service;

import com.mneepay.paymentservice.constants.Constants;
import com.mneepay.paymentservice.dto.subscription.AppRequest;
import com.mneepay.paymentservice.dto.subscription.CustomerSubscriptionRequest;
import com.mneepay.paymentservice.dto.subscription.CustomerSubscriptionResponse;
import com.mneepay.paymentservice.models.subscription.CustomerSubscription;
import com.mneepay.paymentservice.dto.subscription.PlanRequest;
import com.mneepay.paymentservice.models.Merchant;
import com.mneepay.paymentservice.models.subscription.App;
import com.mneepay.paymentservice.models.subscription.Plan;
import com.mneepay.paymentservice.repository.CustomerSubscriptionRepository;
import com.mneepay.paymentservice.repository.MerchantRepository;
import com.mneepay.paymentservice.repository.SubscriptionAppRepository;
import com.mneepay.paymentservice.repository.SubscriptionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionAppRepository subscriptionAppRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private CustomerSubscriptionRepository customerSubscriptionRepository;

    @Value("${mnee.hosted.subpage.base}")
    private String hostedSubscriptionPageBase;

    public App createApp(AppRequest appRequest) throws Exception {
        Optional<Merchant> merchant = merchantRepository.findById(appRequest.getMerchantId());
        if(merchant.isEmpty())
            throw new Exception("Invalid merchant Id");


        App app = App.builder()
                .merchantId(appRequest.getMerchantId())
                .name(appRequest.getName())
                .description(appRequest.getDescription())
                .webhookURL(appRequest.getWebhookURL())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .appKey(Constants.APP_KEY_PREFIX + UUID.randomUUID().toString())
                .build();

        return subscriptionAppRepository.save(app);
    }

    public App updateAppDetails(String appId, AppRequest appRequest) throws Exception {
        Optional<App> optionalApp = subscriptionAppRepository.findById(appId);
        if(optionalApp.isEmpty())
            throw new Exception("App with this id is not present");

        if(appRequest.getName() != null && !appRequest.getName().isEmpty())
            optionalApp.get().setName(appRequest.getName());

        if(appRequest.getDescription() != null && !appRequest.getDescription().isEmpty())
            optionalApp.get().setDescription(appRequest.getDescription());

        if(appRequest.getWebhookURL() != null && !appRequest.getWebhookURL().isEmpty())
            optionalApp.get().setWebhookURL(appRequest.getWebhookURL());

        return subscriptionAppRepository.save(optionalApp.get());
    }

    public App getAppById(String appId) throws Exception {
        return subscriptionAppRepository.findById(appId).orElseThrow(() -> new Exception("Unable to find app with id: " + appId));
    }

    public App  getAppByAppkey(String appKey) throws Exception {
        return subscriptionAppRepository.findByAppKey(appKey).orElseThrow(() -> new Exception("Unable to find app with key: " + appKey));
    }

    public Plan createPlan(PlanRequest planRequest) throws Exception {
        App app = getAppById(planRequest.getAppId());
        Plan plan = new Plan();
        plan.setAppId(app.getId());
        plan.setName(planRequest.getName());
        plan.setCode(planRequest.getCode());
        plan.setAmount(planRequest.getAmount());
        plan.setCurrency(Constants.MNEE_CURRENCY);
        plan.setInterval(planRequest.getInterval());
        plan.setFeatures(planRequest.getFeatures());
        plan.setStatus(Constants.PLAN_ACTIVE_STATUS);
        plan.setCreatedAt(Instant.now());
        plan.setUpdatedAt(Instant.now());

        return subscriptionPlanRepository.save(plan);
    }

    public Plan updatePlan(String planId, PlanRequest planRequest) throws Exception {
        Plan plan = subscriptionPlanRepository.findById(planId).orElseThrow(() -> new Exception("Unable to find plan with id: " + planId));
        if(planRequest.getName() != null && !plan.getName().isEmpty())
            plan.setName(planRequest.getName());

        if(planRequest.getCode() != null && !planRequest.getCode().isEmpty())
            plan.setCode(planRequest.getCode());

        if(planRequest.getAmount() != 0)
            plan.setAmount(planRequest.getAmount());

        if(planRequest.getInterval() != null && !planRequest.getInterval().isEmpty())
            plan.setInterval(planRequest.getInterval());

        if(planRequest.getStatus() != null && !planRequest.getStatus().isEmpty()) {
            if(planRequest.getStatus().equals(Constants.PLAN_ACTIVE_STATUS)
                    || planRequest.getStatus().equals(Constants.PLAN_ARCHIVE_STATUS))
                plan.setStatus(planRequest.getStatus());
            else
                throw new Exception("Invalid plan status: " + planRequest.getStatus());
        }

        if(planRequest.getFeatures() != null && !planRequest.getFeatures().isEmpty())
            plan.setFeatures(planRequest.getFeatures());

        plan.setUpdatedAt(Instant.now());

        return subscriptionPlanRepository.save(plan);
    }

    public List<Plan> getPlanByAppKey(String appKey) throws Exception {
        App app = getAppByAppkey(appKey);
        return subscriptionPlanRepository.findAllByAppId(app.getId());
    }

    public Plan getPlanById(String planId) throws Exception {
        return subscriptionPlanRepository.findById(planId).orElseThrow(() -> new Exception("Plan with plan id: " + planId + " not found."));
    }

    public CustomerSubscription createCustomerSubscription(CustomerSubscriptionRequest customerSubscriptionRequest) throws Exception {
        Plan plan = subscriptionPlanRepository.findById(customerSubscriptionRequest.getPlanId()).orElseThrow(() -> new Exception("plan with id: " + customerSubscriptionRequest.getPlanId() + " doesn't exists."));
        List<String> statuses = List.of(Constants.SUBSCRIPTION_ACTIVE_STATUS);
        boolean hasExisting = customerSubscriptionRepository.existsByAppIdAndCustomerIdAndStatusIn(plan.getAppId(), customerSubscriptionRequest.getCustomerId(), statuses);
        if(hasExisting) {
            throw new Exception("An active subscription already exists");
        }
        App app = subscriptionAppRepository.findById(plan.getAppId()).orElseThrow(() -> new Exception("app not found"));
        Merchant merchant = merchantRepository.findById(app.getMerchantId()).orElseThrow(() -> new Exception("merchant not found"));

        Instant now = Instant.now();
        CustomerSubscription sub = new CustomerSubscription();
        sub.setAppId(plan.getAppId());
        sub.setPlanId(plan.getId());
        sub.setMerchantWalletAddress(merchant.getWalletAddress());
        sub.setAppKey(app.getAppKey());
        sub.setCustomerId(customerSubscriptionRequest.getCustomerId());
        sub.setStatus(Constants.SUBSCRIPTION_PENDING_STATUS);
        sub.setRenewalMode(Constants.SUBSCRIPTION_RENEWAL_MANUAL_MODE);
        sub.setCurrentPeriodStart(now);
        sub.setCreatedAt(now);
        sub.setUpdatedAt(now);

        CustomerSubscription customerSubscription =  customerSubscriptionRepository.save(sub);
        String hostedSubPage = hostedSubscriptionPageBase + "/" + customerSubscription.getId();
        customerSubscription.setHostedSubscriptionPage(hostedSubPage);
        return customerSubscriptionRepository.save(customerSubscription);
    }
    private Instant calculateEnd(Instant start, String interval) {
        if (Constants.YEARLY_SUBSCRIPTION_KEY.equalsIgnoreCase(interval)) {
            return start.plusSeconds(365L * 24 * 60 * 60);
        }
        return start.plusSeconds(30L * 24 * 60 * 60);
    }

    public CustomerSubscription cancelSubscription(String subscriptionId) {
        CustomerSubscription customerSubscription = customerSubscriptionRepository.findById(subscriptionId).orElseThrow(() -> new ExpressionException("Subscription with id : " + subscriptionId + " not found"));
        if(customerSubscription.getStatus().equals(Constants.SUBSCRIPTION_ACTIVE_STATUS) || customerSubscription.getStatus().equals(Constants.SUBSCRIPTION_PENDING_STATUS))
            customerSubscription.setStatus(Constants.SUBSCRIPTION_CANCEL_STATUS);
        else
            throw new ExpressionException("currently plan is in " + customerSubscription.getStatus() + " state");

        customerSubscription.setUpdatedAt(Instant.now());
        return customerSubscriptionRepository.save(customerSubscription);
    }

    public CustomerSubscription getCustomerSubscriptionByAppIdAndCustomerId(String customerId, String appId) {
        Optional<CustomerSubscription> optionalCustomerSubscription = customerSubscriptionRepository.findByAppIdAndCustomerIdAndStatus(appId, customerId, Constants.SUBSCRIPTION_ACTIVE_STATUS);
        if(optionalCustomerSubscription.isPresent()) {
          CustomerSubscription customerSubscription = optionalCustomerSubscription.get();
          if(customerSubscription.getStatus().equals(Constants.SUBSCRIPTION_ACTIVE_STATUS)) {
              if(customerSubscription.getCurrentPeriodEnd() != null && Instant.now().compareTo(customerSubscription.getCurrentPeriodEnd()) >= 0) {
                  customerSubscription.setStatus(Constants.SUBSCRIPTION_EXPIRE_STATUS);
                  customerSubscriptionRepository.save(customerSubscription);
                  return null;
              } else {
                  return customerSubscription;
              }
          }
        }

        return null;
    }

    public List<App> getAllAppByMerchantId(String merchantId) {
        return subscriptionAppRepository.findAllByMerchantId(merchantId);
    }

    public List<CustomerSubscription> getAllSubscriptionByAppKey(String appKey) {
        return customerSubscriptionRepository.findAllByAppKey(appKey);
    }

    public CustomerSubscription getSubscriptionById(String subId) throws Exception{
        return customerSubscriptionRepository.findById(subId).orElseThrow(()->new Exception("unable to find subscription with id: " + subId));
    }

    public CustomerSubscription updateCustomerSubscriptionDetailsMap(String subId, String ticketId, Map<String, String> detailsMap) throws Exception {
        CustomerSubscription customerSubscription = customerSubscriptionRepository.findById(subId).orElseThrow(() -> new ExpressionException("Unable to find subscription with id: "+ subId));
        Plan plan = subscriptionPlanRepository.findById(customerSubscription.getPlanId()).orElseThrow(() -> new ExpressionException("Unable to find plan"));
        List<Map<String, String>> dMapL = customerSubscription.getDetailsMapList();
        if(dMapL == null) {
            dMapL = new ArrayList<>();
        }

        detailsMap.put("ticketId", ticketId);

        dMapL.add(detailsMap);
        if(detailsMap.get("status").equals("SUCCESS") && customerSubscription.getStatus().equals(Constants.SUBSCRIPTION_PENDING_STATUS)) {
            customerSubscription.setStatus(Constants.SUBSCRIPTION_ACTIVE_STATUS);
        }

        if(customerSubscription.getCurrentPeriodEnd() == null) {
            customerSubscription.setCurrentPeriodEnd(calculateEnd(customerSubscription.getCurrentPeriodStart(), plan.getInterval()));
        } else {
            customerSubscription.setCurrentPeriodEnd(calculateEnd(customerSubscription.getCurrentPeriodEnd(), plan.getInterval()));
        }

        customerSubscription.setDetailsMapList(dMapL);

        // send post request asynchronously to the app webhookURL
        return customerSubscriptionRepository.save(customerSubscription);

    }


}
