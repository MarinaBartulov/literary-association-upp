package team16.literaryassociation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.config.EndpointConfig;
import team16.literaryassociation.config.RestConfig;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class TestService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig configuration;

    public ResponseEntity<String> getResponseFromPC() throws URISyntaxException {
        ResponseEntity<String> response
                = restTemplate.getForEntity(getEndpoint(), String.class);
        return response;
    }

    private URI getEndpoint() throws URISyntaxException {
        return new URI(  configuration.url() + EndpointConfig.PAYMENT_SERVICE_PROVIDER_BASE_URL + "/api/test");
    }
}
