package team16.literaryassociation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.dto.OrderDTO;
import team16.literaryassociation.dto.OrderRequestDTO;
import team16.literaryassociation.dto.OrderResponseDTO;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.services.interfaces.MerchantService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderRequestDTO dto){

        Merchant merchant = this.merchantService.findOne(dto.getMerchantId());
        if(merchant == null){
            return ResponseEntity.badRequest().body("Merchant with that id doesn't exist.");
        }
        ResponseEntity<OrderResponseDTO> response;
        try {
            response = restTemplate.postForEntity("https://localhost:8083/psp-service/api/payments",
                    new OrderDTO(null, merchant.getMerchantId(), merchant.getMerchantEmail(), merchant.getMerchantPassword(),
                            dto.getCurrency(), dto.getAmount(), merchant.getMerchantSuccessUrl(), merchant.getMerchantFailedUrl(),
                            merchant.getMerchantErrorUrl()), OrderResponseDTO.class);
            System.out.println(response.getBody().getOrderId());
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error occurred while sending order on payment concentrator.");
        }

        return ResponseEntity.ok(response.getBody());
    }
}
