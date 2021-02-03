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
import team16.literaryassociation.services.interfaces.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderRequestDTO dto){

        Merchant merchant = this.merchantService.findOne(dto.getMerchantId());
        if(merchant == null){
            return ResponseEntity.badRequest().body("Merchant with that id doesn't exist.");
        }
        OrderResponseDTO orderResponseDTO = this.orderService.createOrder(dto, merchant);
        return ResponseEntity.ok(orderResponseDTO);
    }
}
