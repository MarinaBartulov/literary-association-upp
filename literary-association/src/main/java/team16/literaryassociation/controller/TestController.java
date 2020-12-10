package team16.literaryassociation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.services.TestService;

import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping(value="/pay")
    public ResponseEntity<?> pay() throws URISyntaxException {
        ResponseEntity<String> response = testService.getResponseFromPC();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value="/pay", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> pay(@RequestBody String price){

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
