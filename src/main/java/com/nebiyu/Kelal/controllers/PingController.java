package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.dto.response.PingResponse;
import com.nebiyu.Kelal.services.PingService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/ping")
public class PingController {
    @Autowired
    PingService pingService;



    @PostMapping("/ping")
    public ResponseEntity<PingResponse> getPing(){

        PingResponse response = pingService.ping();
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);

    }




}


