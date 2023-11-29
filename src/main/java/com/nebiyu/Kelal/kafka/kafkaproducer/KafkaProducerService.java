//package com.nebiyu.Kelal.kafka.kafkaproducer;
//
//import com.nebiyu.Kelal.dto.response.TransactionResponseId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaProducerService {
//
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//
//    @Autowired
//    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendTransactionMessage(TransactionResponseId.UserData transactionResponse) {
//        kafkaTemplate.send("transaction-topic", transactionResponse);
//    }
//}
