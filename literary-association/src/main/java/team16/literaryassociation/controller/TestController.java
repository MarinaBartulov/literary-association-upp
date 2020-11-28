package team16.literaryassociation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    @GetMapping(value="/pay")
    public ResponseEntity<?> pay(){

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/pay", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> pay(@RequestBody String price){

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
