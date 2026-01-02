package com.mneepay.paymentservice.controller;

import com.mneepay.paymentservice.constants.Constants;
import com.mneepay.paymentservice.dto.CheckoutSessionResponse;
import com.mneepay.paymentservice.dto.UpdateDetailsMapRequest;
import com.mneepay.paymentservice.dto.subscription.*;
import com.mneepay.paymentservice.models.CheckoutSession;
import com.mneepay.paymentservice.models.subscription.App;
import com.mneepay.paymentservice.models.subscription.CustomerSubscription;
import com.mneepay.paymentservice.models.subscription.Plan;
import com.mneepay.paymentservice.service.SubscriptionService;
import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/create-app")
    public ResponseEntity<AppResponse> createApp(@RequestBody AppRequest appRequest) {
        try {
            App app = subscriptionService.createApp(appRequest);
            AppResponse appResponse = AppResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .app(app)
                    .build();

            return ResponseEntity.ok(appResponse);
        } catch(Exception ex) {
            AppResponse appResponse = AppResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(appResponse);
        }
    }

    @PostMapping("/{appId}/update-app")
    public ResponseEntity<AppResponse> updateApp(@PathVariable String appId,  @RequestBody AppRequest appRequest) {
        try {
            App app = subscriptionService.updateAppDetails(appId, appRequest);
            AppResponse appResponse = AppResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .app(app)
                    .build();

            return ResponseEntity.ok(appResponse);
        } catch(Exception ex) {
            AppResponse appResponse = AppResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(appResponse);
        }
    }

    @GetMapping("/{appId}/get-app")
    public ResponseEntity<AppResponse> getAppById(@PathVariable String appId) {
        try {
            App app = subscriptionService.getAppById(appId);
            AppResponse appResponse = AppResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .app(app)
                    .build();

            return ResponseEntity.ok(appResponse);
        } catch(Exception ex) {
            AppResponse appResponse = AppResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(appResponse);
        }
    }

    @GetMapping("/{appKey}/get-app-by-key")
    public ResponseEntity<AppResponse> getAppByKey(@PathVariable String appKey) {
        try {
            App app = subscriptionService.getAppByAppkey(appKey);
            AppResponse appResponse = AppResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .app(app)
                    .build();

            return ResponseEntity.ok(appResponse);
        } catch(Exception ex) {
            AppResponse appResponse = AppResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(appResponse);
        }
    }

    @GetMapping("{merchantId}/get-all-app")
    public ResponseEntity<List<App>> getAllAppByMerchant(@PathVariable String merchantId) {
        List<App> apps = subscriptionService.getAllAppByMerchantId(merchantId);
        return ResponseEntity.ok(apps);
    }

    @GetMapping("{appKey}/get-all-sub")
    public ResponseEntity<List<CustomerSubscription>> getAllCustomerSubscription(@PathVariable String appKey) {
        List<CustomerSubscription> cs = subscriptionService.getAllSubscriptionByAppKey(appKey);
        return ResponseEntity.ok(cs);
    }

    @GetMapping("{subId}/get-sub-by-id")
    public ResponseEntity<CustomerSubscriptionResponse> getSubscriptionById(@PathVariable String subId) {
        try {
            CustomerSubscription customerSubscription = subscriptionService.getSubscriptionById(subId);
            CustomerSubscriptionResponse customerSubscriptionResponse = CustomerSubscriptionResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .customerSubscription(customerSubscription)
                    .build();

            return ResponseEntity.ok(customerSubscriptionResponse);
        } catch(Exception ex) {
            CustomerSubscriptionResponse customerSubscriptionResponse = CustomerSubscriptionResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(customerSubscriptionResponse);
        }
    }

    @PostMapping("/create-plan")
    public ResponseEntity<PlanResponse> createPlan(@RequestBody PlanRequest planRequest) {
        try {
            Plan plan = subscriptionService.createPlan(planRequest);
            PlanResponse planResponse = PlanResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .plan(plan)
                    .build();

            return ResponseEntity.ok(planResponse);
        } catch(Exception ex) {
            PlanResponse planResponse = PlanResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(planResponse);
        }
    }

    @PostMapping("{planId}/update-plan")
    public ResponseEntity<PlanResponse> updatePlan(@PathVariable String planId, @RequestBody PlanRequest planRequest) {
        try {
            Plan plan = subscriptionService.updatePlan(planId, planRequest);
            PlanResponse planResponse = PlanResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .plan(plan)
                    .build();

            return ResponseEntity.ok(planResponse);
        } catch(Exception ex) {
            PlanResponse planResponse = PlanResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(planResponse);
        }
    }

    @GetMapping("{appKey}/get-all-plan-by-app-key")
    public ResponseEntity<List<Plan>> getPlanByAppKey(@PathVariable String appKey) {
        try {
            List<Plan> list = subscriptionService.getPlanByAppKey(appKey);
            return ResponseEntity.ok(list);
        } catch(Exception ex) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }

    @GetMapping("{planId}/get-plan-by-id")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable String planId) {
        try {
            Plan plan = subscriptionService.getPlanById(planId);
            PlanResponse planResponse = PlanResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .plan(plan)
                    .build();

            return ResponseEntity.ok(planResponse);

        } catch(Exception ex) {
            PlanResponse planResponse = PlanResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(planResponse);
        }
    }

    @PostMapping("/create-customer-subscription")
    public ResponseEntity<CustomerSubscriptionResponse> getPlanById(@RequestBody CustomerSubscriptionRequest customerSubscriptionRequest) {
        try {
            CustomerSubscription customerSubscription = subscriptionService.createCustomerSubscription(customerSubscriptionRequest);
            CustomerSubscriptionResponse customerSubscriptionResponse = CustomerSubscriptionResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .customerSubscription(customerSubscription)
                    .build();

            return ResponseEntity.ok(customerSubscriptionResponse);

        } catch(Exception ex) {
            CustomerSubscriptionResponse customerSubscriptionResponse = CustomerSubscriptionResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(customerSubscriptionResponse);
        }
    }

    @PostMapping("/{subscriptionId}/cancel-subscription")
    public ResponseEntity<CustomerSubscriptionResponse> cancelCustomerSubscription(@PathVariable String subscriptionId) {
        try {
            CustomerSubscription customerSubscription = subscriptionService.cancelSubscription(subscriptionId);
            CustomerSubscriptionResponse customerSubscriptionResponse = CustomerSubscriptionResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .customerSubscription(customerSubscription)
                    .build();

            return ResponseEntity.ok(customerSubscriptionResponse);
        } catch(Exception ex) {
            CustomerSubscriptionResponse customerSubscriptionResponse = CustomerSubscriptionResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(customerSubscriptionResponse);
        }
    }

    @GetMapping("/get-customer-subscription")
    public ResponseEntity<CustomerSubscription> getCustomerSubscription(@RequestParam String customerId, @RequestParam String appId) {
        CustomerSubscription customerSubscription = subscriptionService.getCustomerSubscriptionByAppIdAndCustomerId(customerId, appId);
        return ResponseEntity.ok(customerSubscription);
    }

    @PostMapping("{subId}/update-sub-details-map")
    public ResponseEntity<CustomerSubscriptionResponse> updateCsDetailsMap(@PathVariable String subId, @RequestBody UpdateDetailsMapRequest request) {
        try {
            CustomerSubscription customerSubscription = subscriptionService.updateCustomerSubscriptionDetailsMap(subId, request.getTicketId(), request.getDetailsMap());
            CustomerSubscriptionResponse customerSubscriptionResponse = CustomerSubscriptionResponse.builder()
                    .status(Constants.SUCCESS)
                    .errorMessage(null)
                    .customerSubscription(customerSubscription)
                    .build();

            return ResponseEntity.ok(customerSubscriptionResponse);
        } catch(Exception ex) {
            CustomerSubscriptionResponse customerSubscriptionResponse = CustomerSubscriptionResponse.builder()
                    .status(Constants.FAILURE)
                    .errorMessage(ex.getMessage())
                    .build();

            return ResponseEntity.ok(customerSubscriptionResponse);
        }
    }
 }
