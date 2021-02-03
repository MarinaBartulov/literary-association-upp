package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.config.EndpointConfig;
import team16.literaryassociation.config.RestConfig;
import team16.literaryassociation.dto.BillingPlanDTO;
import team16.literaryassociation.dto.SubscriptionRequestDTO;
import team16.literaryassociation.dto.SubscriptionResponseDTO;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.model.Subscription;
import team16.literaryassociation.repository.SubscriptionRepository;
import team16.literaryassociation.services.interfaces.SubscriptionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig configuration;


    @Override
    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<BillingPlanDTO> getMerchantBillingPlans(String merchantEmail) {

        System.out.println("Usao u get merchant billing plans servisnu metodu, salje zahtev psp-u");
        List<BillingPlanDTO> billingPlanDTOS = new ArrayList<>();
        HttpEntity<String> request = new HttpEntity<>(merchantEmail);
        ParameterizedTypeReference<List<BillingPlanDTO>> responseType = new ParameterizedTypeReference<List<BillingPlanDTO>>() {};
        try {
            ResponseEntity<List<BillingPlanDTO>> resp = restTemplate.exchange(configuration.url() + EndpointConfig.PAYMENT_SERVICE_PROVIDER_BASE_URL + "/api/billing-plans",
                    HttpMethod.POST, request, responseType);
            billingPlanDTOS = resp.getBody();

        } catch (RestClientException e) {
            e.printStackTrace();
        }
        System.out.println("Vratio se sa psp-a");
        System.out.println(billingPlanDTOS);
        return billingPlanDTOS;
    }

    @Override
    public Boolean readerHasSubscription(Reader reader, Merchant merchant) {
        List<Subscription> subscription = subscriptionRepository.readerHasSubscription(reader.getId(), merchant.getId(), LocalDate.now());
        if(subscription.size() == 0){
            return false;
        }
        else return true;
    }

    @Override
    public ResponseEntity<?> sendSubscriptionToPC(SubscriptionRequestDTO dto, Reader reader, Merchant merchant) {
        System.out.println("Merchant: ");
        System.out.println(merchant.getMerchantEmail());
        dto.setMerchantEmail(merchant.getMerchantEmail());

        HttpEntity<SubscriptionRequestDTO> request = new HttpEntity<>(dto);
        ResponseEntity<SubscriptionResponseDTO> response = null;

        try {
            response = restTemplate.exchange(configuration.url() + EndpointConfig.PAYMENT_SERVICE_PROVIDER_BASE_URL + "/api/subscriptions/subscribe",
                    HttpMethod.POST, request, SubscriptionResponseDTO.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return response;
    }


}
