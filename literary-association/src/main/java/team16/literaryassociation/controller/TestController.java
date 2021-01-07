package team16.literaryassociation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.PaymentRequestDTO;
import team16.literaryassociation.services.TestService;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    // metodu dodaj u neki smisleni kontroler, a ne u Test
    @PostMapping(value="/pay")
    //@PreAuthorize("hasAuthority('create_order')")
    public ResponseEntity<?> pay(@RequestBody PaymentRequestDTO dto) throws Exception {
        System.out.println("Uslo ovde");
        ResponseEntity<?> response = testService.getResponseFromPC(dto);
        return response;
    }

    @GetMapping(value="/pay", produces = "application/json")
    public ResponseEntity<?> pay(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
