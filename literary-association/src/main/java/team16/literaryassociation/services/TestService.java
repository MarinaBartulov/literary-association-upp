package team16.literaryassociation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.config.EndpointConfig;
import team16.literaryassociation.config.RestConfig;
import team16.literaryassociation.dto.FirstPCResponseDTO;
import team16.literaryassociation.dto.PaymentDetailsDTO;
import team16.literaryassociation.dto.PaymentRequestDTO;
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

    public ResponseEntity<?> getResponseFromPC(PaymentRequestDTO price) throws Exception {
        // dobaviti MERCHANT_ID prodavca ovog LU-a ili ako imamo vise prodavaca u LU, onda prodavca onih proizvoda koji su izabrani
        Merchant merchant = merchantRepository.getOne(1L);
        // cenu dobijam sa front-a iz korpe
        double priceD;
        try {
            priceD = Double.parseDouble(price.getPrice());
        } catch (Exception e) {
            throw new Exception("Price must be decimal value.");
        }
        ResponseEntity<FirstPCResponseDTO> response
                = restTemplate.postForEntity(getEndpoint(),
                new PaymentDetailsDTO(1L, merchant.getMerchant_id(), merchant.getPassword(), priceD), FirstPCResponseDTO.class);
        System.out.println(response.getBody());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private URI getEndpoint() throws URISyntaxException {
        return new URI(  configuration.url() + EndpointConfig.PAYMENT_SERVICE_PROVIDER_BASE_URL + "/api/payments");
    }
}
