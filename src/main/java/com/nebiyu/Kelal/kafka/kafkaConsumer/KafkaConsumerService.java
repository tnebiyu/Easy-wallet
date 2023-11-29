//package com.nebiyu.Kelal.kafka.kafkaConsumer;
//
//import com.nebiyu.Kelal.dto.response.TransactionResponseId;
//import com.nebiyu.Kelal.services.TwilioService;
//import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.kafka.annotation.KafkaListener;
////import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaConsumerService {
//    @Autowired
//    private TwilioService twilioService;
//
//    @KafkaListener(topics = "transaction-topic", groupId = "transaction12")
//    public TransactionResponseId processTransactionMessage(TransactionResponseId.Data transactionData) {
//        System.out.println("Received transaction: " + transactionData.getUser_data());
//        sendSmsNotification(transactionData.getUser_data());
//        return TransactionResponseId.builder().data(transactionData).error(false).error_msg("").build();
//    }
//    private void sendSmsNotification(TransactionResponseId.UserData transactionResponse) {
//
//        twilioService.sendSms(transactionResponse, "your transaction of amount "
//        + transactionResponse.getAmount() + " is transferred to " + transactionResponse.getReceiverId());
//        System.out.println("SMS notification sent successfully.");
//    }
////    private void notifyUsers(TransactionResponseId.Data transactionData) {
////        // Notify subscribed users via WebSocket
////        messagingTemplate.convertAndSendToUser("username", "/topic/notifications", transactionData);
////        System.out.println("WebSocket notification sent successfully.");
////    }
//}
