package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.response.PingResponse;
import com.nebiyu.Kelal.response.TransactionHistoryResponse;
import com.nebiyu.Kelal.services.PingService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import com.nebiyu.Kelal.services.TransactionService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/ping")
public class PingController {
    @Autowired
    PingService pingService;
   @Autowired
    TransactionService transactionService;


    @PostMapping("/ping")
    public ResponseEntity<PingResponse> getPing(){

        PingResponse response = pingService.ping();
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);

    }
    @PostMapping(value = "/getTransaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<TransactionHistoryResponse> getTransactionHistoryPerUserId(@RequestParam Long id){
        TransactionHistoryResponse response = transactionService.getTransactionHistoryWithId(id);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }



}


