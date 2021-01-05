package team16.literaryassociation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.config.EndpointConfig;
import team16.literaryassociation.config.RestConfig;
import team16.literaryassociation.dto.*;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.repository.MerchantRepository;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class TestService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig configuration;

    @Autowired
    private MerchantRepository merchantRepository;

    public ResponseEntity<?> getResponseFromPC(PaymentRequestDTO dto) throws Exception {
        // dobaviti MERCHANT_ID prodavca ovog LU-a ili ako imamo vise prodavaca u LU, onda prodavca onih proizvoda koji su izabrani
        Merchant merchant = merchantRepository.getOne(1L);
        // cenu i valutu dobijam sa front-a iz korpe
        double price;
        try {
            price = Double.parseDouble(dto.getPrice());
        } catch (Exception e) {
            throw new Exception("Price must be decimal value.");
        }
        ResponseEntity<OrderResponseDTO> response
                = restTemplate.postForEntity(getEndpoint(),
                new OrderDTO(null, merchant.getMerchantId(), merchant.getMerchantEmail(), merchant.getPassword(),
                        dto.getCurrency(), price, merchant.getMerchantSuccessUrl(), merchant.getMerchantFailedUrl(),
                        merchant.getMerchantErrorUrl()), OrderResponseDTO.class);
        System.out.println(response.getBody().getOrderId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private URI getEndpoint() throws URISyntaxException {
        return new URI(  configuration.url() + EndpointConfig.PAYMENT_SERVICE_PROVIDER_BASE_URL + "/api/payments");
    }

    public ResponseEntity<?> sendSubscriptionToPC(SubscriptionRequestDTO dto) throws Exception {

        Merchant merchant = merchantRepository.getOne(1L);
        if(merchant == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        dto.setMerchantEmail(merchant.getMerchantEmail());
        dto.setMerchantId(merchant.getMerchantId());

        HttpEntity<SubscriptionRequestDTO> request = new HttpEntity<>(dto);
        ResponseEntity<String> response = null;

        try {
            response = restTemplate.exchange(configuration.url() + EndpointConfig.PAYMENT_SERVICE_PROVIDER_BASE_URL + "/api/payments/subscribe",
                    HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        System.out.println(response.getBody()); //vraca samo redirection url
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
