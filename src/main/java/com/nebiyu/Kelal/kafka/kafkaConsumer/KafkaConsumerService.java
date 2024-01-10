package com.nebiyu.Kelal.kafka.kafkaConsumer;
import com.nebiyu.Kelal.dao.TransferRequestWithPhone;
import com.nebiyu.Kelal.services.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private SmsService smsService;

    @KafkaListener(topics = "transaction-topic", groupId = "transaction12")
    public void processTransactionMessage(TransferRequestWithPhone request) {
        System.out.println("Received transaction: " + request);
        sendSmsNotification(request.getSenderPhone(), " amount " + request.getAmount() +
                " is transferred to your account");
    }
    private void sendSmsNotification(String to, String message) {

        smsService.sendNotify(to, message);
        System.out.println("SMS notification sent successfully.");
    }
//    private void notifyUsers(TransactionResponseId.Data transactionData) {

//        messagingTemplate.convertAndSendToUser("username", "/topic/notifications", transactionData);
//        System.out.println("WebSocket notification sent successfully.");
//    }
}
