//package com.nebiyu.Kelal.controllers;
//
//import com.nebiyu.Kelal.dto.response.TransactionResponseId;
//import com.nebiyu.Kelal.kafka.kafkaConsumer.KafkaConsumerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/transactions")
//public class KafkaController {
//
//    @Autowired
//    private KafkaConsumerService kafkaConsumerService;
//
//    @GetMapping("/process")
//    public ResponseEntity<TransactionResponseId> processTransaction() {
//        try {
//            // Assuming the Kafka Consumer Service method returns TransactionResponseId
//            TransactionResponseId response = kafkaConsumerService.processTransactionMessage();
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            // Handle exceptions if needed
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
