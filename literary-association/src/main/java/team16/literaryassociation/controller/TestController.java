package team16.literaryassociation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.PaymentDetailsDTO;
import team16.literaryassociation.dto.PaymentRequestDTO;
import team16.literaryassociation.services.TestService;

import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    // metodu dodaj u neki smisleni kontroler, a ne u Test
    @PostMapping(value="/pay")
    public ResponseEntity<?> pay(@RequestBody PaymentRequestDTO price) throws Exception {
        ResponseEntity<?> response = testService.getResponseFromPC(price);
        return response;
    }

    @GetMapping(value="/pay", produces = "application/json")
    public ResponseEntity<?> pay(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
