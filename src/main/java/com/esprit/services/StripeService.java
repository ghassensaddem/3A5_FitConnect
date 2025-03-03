package com.esprit.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class StripeService {

    public StripeService() {
        Stripe.apiKey = "sk_test_51QxQ0L013eLvCqd8X9t4TvnvwixFB37Dp9vmqCKckowtzns31pI4aS1wRMqyLSyG4HHdebz8iYeRsieM41Nyt0E300y297MPiL";
    }

    public PaymentIntent createPaymentIntent(int amount, String currency, String customerId, String paymentMethodId) throws StripeException {
        Map<String, Object> paymentIntentParams = new HashMap<>();
        paymentIntentParams.put("amount", amount);
        paymentIntentParams.put("currency", currency);
        paymentIntentParams.put("customer", customerId);
        paymentIntentParams.put("payment_method", paymentMethodId);
        paymentIntentParams.put("confirm", true);
        paymentIntentParams.put("off_session", true);

        return PaymentIntent.create(paymentIntentParams);
    }

    public PaymentMethod attachPaymentMethodToCustomer(String paymentMethodId, String customerId) throws StripeException {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);

        Map<String, Object> attachParams = new HashMap<>();
        attachParams.put("customer", customerId);
        paymentMethod.attach(attachParams);

        return paymentMethod;
    }

    public Customer setDefaultPaymentMethod(String customerId, String paymentMethodId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);

        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("invoice_settings", Map.of("default_payment_method", paymentMethodId));

        return customer.update(updateParams);
    }
}