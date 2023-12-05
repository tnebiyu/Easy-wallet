package com.nebiyu.Kelal.services;


import com.nebiyu.Kelal.configuration.JWTService;
//import com.nebiyu.Kelal.kafka.kafkaproducer.KafkaProducerService;
import com.nebiyu.Kelal.dto.request.TransferRequestWithPhone;
import com.nebiyu.Kelal.dto.response.*;
import com.nebiyu.Kelal.model.TransactionModel;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.TransactionRepository;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.dto.request.TransferRequestWithEmail;
import com.nebiyu.Kelal.dto.request.TransferRequestWithId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private SmsService smsService;
//    @Autowired

   // private KafkaProducerService kafkaProducer;
    @Autowired
    private JWTService jwtService;
    @Transactional
    @Async
    public TransferResponse transferMoneyByEmail(TransferRequestWithEmail request, String jwtToken) {

        try {

            Claims claims = jwtService.verify(jwtToken);
            String senderEmail =(String) claims.get("email");


            if (!Objects.equals(senderEmail, request.getSenderEmail()) && isTokenExpired(jwtToken)) {

                return TransferResponse.builder().error(true).error_msg("User is not authenticated or token is expired").build();
            }


            Optional<User> senderOptional = userRepository.findByEmail(senderEmail);
            Optional<User> receiverOptional = userRepository.findByEmail(request.getReceiverEmail());
            if (senderOptional.isEmpty() || receiverOptional.isEmpty()) {

                return TransferResponse.builder().error(true).error_msg("Sender or receiver is not found").build();
            }
            if (senderOptional.equals(receiverOptional)){

                return TransferResponse.builder().error(true).error_msg("Sender and Receiver are same").build();

            }



            User sender = senderOptional.get();
            User receiver = receiverOptional.get();

            if (sender.getBalance().compareTo(request.getAmount()) <=0) {

               return TransferResponse.builder().error(true).error_msg("no balance please recharge your account").build();
            }
            sender.setBalance(sender.getBalance().subtract(request.getAmount()));
            receiver.setBalance(receiver.getBalance().add(request.getAmount()));



            TransactionModel transaction = TransactionModel.builder().sender(sender)
                    .receiver(receiver)
                    .amount(request.getAmount())
                    .timestamp(new Date())
                    .build();

            TransferResponse.UserData userData = TransferResponse.UserData.builder()
                    .senderEmail(request.getSenderEmail())
                    .receiverEmail(request.getReceiverEmail())
                    .newBalance(sender.getBalance())
                    .build();




            TransferResponse.Data data = TransferResponse.Data.builder()
                    .user_data(userData)
                    .build();

userRepository.save(sender);
userRepository.save(receiver);
transactionRepository.save(transaction);



            return TransferResponse.builder().error_msg("").error(false).data(data).build();
        } catch (Exception e) {
            return TransferResponse.builder().error(true).error_msg("Money transfer failed: " + e.getMessage()).build();
        }
    }
    @Transactional
    @Async
    public TransactionResponseId transferMoneyById(TransferRequestWithId request, String jwtToken) {
        try {
            Claims claims = jwtService.verify(jwtToken);
            String senderId =(String) claims.get("email");


            if (!Objects.equals(senderId, request.getSenderId()) && isTokenExpired(jwtToken)) {

                return TransactionResponseId.builder().error(true).error_msg("User is not authenticated or token is expired").build();
            }

            Optional<User> sender = userRepository.findById(request.getSenderId());
            Optional<User> receiver = userRepository.findById(request.getReceiverId());
              if (sender.isEmpty() || receiver.isEmpty()){
                  return TransactionResponseId.builder().error(true).error_msg("Sender or Receiver are not found").build();
              }

                if (Objects.equals(sender, receiver)) {
                    return TransactionResponseId.builder().error(true).error_msg("Sender and Receiver are the same").build();
                }

                if (sender.get().getBalance().compareTo(request.getAmount()) <= 0) {

                    return TransactionResponseId.builder().error(true).error_msg("no balance please recharge your account").build();
                }
                sender.get().setBalance(sender.get().getBalance().subtract(request.getAmount()));
                receiver.get().setBalance(receiver.get().getBalance().add(request.getAmount()));
                var transactionResponseSenderUserData = TransactionResponseId.UserData.builder()
                        .amount(request.getAmount()).receiverId(request.getReceiverId())
                        .timestamp(new Date()).build();


                TransactionModel transaction = TransactionModel.builder().sender(sender.get())
                        .receiver(receiver.get())
                        .amount(request.getAmount())
                        .timestamp(new Date())
                        .build();
                userRepository.save(sender.get());
                userRepository.save(receiver.get());
                transactionRepository.save(transaction);
               // kafkaProducer.sendTransactionMessage(transactionResponseSenderUserData);


                var data = TransactionResponseId.Data.builder().user_data(transactionResponseSenderUserData).build();
                return TransactionResponseId.builder().data(data).error(false).error_msg("").build();

        } catch (Exception e) {

            return TransactionResponseId.builder().error(true).error_msg(e.toString()).build();

        }
    }
    @Transactional
    @Async
    public TransactionResponseViaPhone transferMoneyViaPhone(TransferRequestWithPhone request, String jwtToken) {
        try {
            Claims claims = jwtService.verify(jwtToken);
            String senderPhone =(String) claims.get("email");


            if (!Objects.equals(senderPhone, request.getSenderPhone()) && isTokenExpired(jwtToken)) {

                return TransactionResponseViaPhone.builder().error(true).error_msg("User is not authenticated or token is expired").build();
            }

            Optional<User> sender = userRepository.findByPhoneNumber(request.getSenderPhone());
            Optional<User> receiver = userRepository.findByPhoneNumber(request.getReceiverPhone());
            if (sender.isEmpty() || receiver.isEmpty()){
                return TransactionResponseViaPhone.builder().error(true).error_msg("Sender or Receiver are not found").build();
            }

            if (Objects.equals(sender, receiver)) {
                return TransactionResponseViaPhone.builder().error(true).error_msg("Sender and Receiver are the same").build();
            }

            if (sender.get().getBalance().compareTo(request.getAmount()) <= 0) {

                return TransactionResponseViaPhone.builder().error(true).error_msg("no balance please recharge your account").build();
            }
            sender.get().setBalance(sender.get().getBalance().subtract(request.getAmount()));
            receiver.get().setBalance(receiver.get().getBalance().add(request.getAmount()));
            var userData = TransactionResponseViaPhone.UserData.builder()
                    .amount(request.getAmount()).receiverPhone(request.getReceiverPhone())
                    .timestamp(new Date()).build();


            TransactionModel transaction = TransactionModel.builder().sender(sender.get())
                    .receiver(receiver.get())
                    .amount(request.getAmount())
                    .timestamp(new Date())
                    .build();
            userRepository.save(sender.get());
            userRepository.save(receiver.get());
            transactionRepository.save(transaction);
            smsService.sendNotify(request.getSenderPhone(), " amount " + request.getAmount() +
                    " is transferred to " + transaction.getReceiver().getPhoneNumber() + " transaction id " + transaction.getId() +
                    " at the date of " + transaction.getTimestamp() + " your current balance is " + sender.get().getBalance()
                    );
            smsService.sendNotify(request.getReceiverPhone(), " amount " + request.getAmount() +
                    " is transferred to your account" + " transaction id " + transaction.getId() +
                    " at the date of " + transaction.getTimestamp() + " your current balance is " + receiver.get().getBalance());
            // kafkaProducer.sendTransactionMessage(transactionResponseSenderUserData);


            var data = TransactionResponseViaPhone.Data.builder().user_data(userData).build();
            return TransactionResponseViaPhone.builder().data(data).error(false).error_msg("").build();

        } catch (Exception e) {

            return TransactionResponseViaPhone.builder().error(true).error_msg(e.toString()).build();

        }
    }
    public TransactionHistoryResponse getTransactionHistoryWithId(Long id){
        List<TransactionHistoryResponse.SentTransaction> sentTransactionList = new ArrayList<>();
        List<TransactionHistoryResponse.ReceivedTransaction> receivedTransactionList = new ArrayList<>();
        try{
            List<TransactionModel> userTransactions = transactionRepository.findAllTransactionsForUser(id);
            if (userTransactions.isEmpty()){
                return TransactionHistoryResponse.builder().error(true).error_msg("user not found").build();
            }
            userTransactions.sort(Comparator.comparing(TransactionModel::getTimestamp).reversed());


            for (TransactionModel transaction : userTransactions){


                if (Objects.equals(transaction.getSender().getId(), id)) {

                    var senderData = TransactionHistoryResponse.SentTransaction.builder()
                            .balance(transaction.getAmount())
                            .receiver(transaction.getReceiver().getEmail())
                            .timestamp(transaction.getTimestamp())
                            .build();
                    sentTransactionList.add(senderData);
                } else if (Objects.equals(transaction.getReceiver().getId(), id)) {

                    var receiverData = TransactionHistoryResponse.ReceivedTransaction.builder()
                            .balance(transaction.getAmount())
                            .sender(transaction.getSender().getEmail())
                            .timestamp(transaction.getTimestamp())
                            .build();
                    receivedTransactionList.add(receiverData);
                }
                }


       var userData = TransactionHistoryResponse.UserData.builder().sent(sentTransactionList)
                    .received(receivedTransactionList).build();
            var data = TransactionHistoryResponse.Data.builder().userData(userData).build();


            return TransactionHistoryResponse.builder()
                    .data(data)
                    .error(false)
                    .error_msg("")
                    .build();

        }
        catch (Exception e) {
            return TransactionHistoryResponse.builder().error(true).error_msg(e.toString()).build();
        }
    }
public RecentTransactionResponse getRecentTransaction(Long id){
    List<RecentTransactionResponse.SentTransaction> sentTransactionList = new ArrayList<>();
    List<RecentTransactionResponse.ReceivedTransaction> receivedTransactionList = new ArrayList<>();

try{
    List<TransactionModel> userTransactions = transactionRepository.findAllTransactionsForUser(id);
    if (userTransactions.isEmpty()){
        return RecentTransactionResponse.builder().error(true).error_msg("user not found").build();
    }
    userTransactions.sort(Comparator.comparing(TransactionModel::getTimestamp).reversed());
    int transactionLimit = Math.min(userTransactions.size(), 6);

    for (int i=0; i<transactionLimit; i++) {

      TransactionModel transaction = userTransactions.get(i);
        if (Objects.equals(transaction.getSender().getId(), id)) {

            var senderData = RecentTransactionResponse.SentTransaction.builder()
                    .balance(transaction.getAmount())
                    .receiver(transaction.getReceiver().getEmail())
                    .timestamp(transaction.getTimestamp())
                    .build();
            sentTransactionList.add(senderData);
        } else if (Objects.equals(transaction.getReceiver().getId(), id)) {

            var receiverData = RecentTransactionResponse.ReceivedTransaction.builder()
                    .balance(transaction.getAmount())
                    .sender(transaction.getSender().getEmail())
                    .timestamp(transaction.getTimestamp())
                    .build();
            receivedTransactionList.add(receiverData);
        }
    }


    var userData = RecentTransactionResponse.UserData.builder().sent(sentTransactionList)
            .received(receivedTransactionList).build();
    var data = RecentTransactionResponse.Data.builder().userData(userData).build();


    return RecentTransactionResponse.builder()
            .data(data)
            .error(false)
            .error_msg("")
            .build();

}
catch (Exception e){
    return RecentTransactionResponse.builder().error(true).error_msg(e.toString()).build();

}

}
    private boolean isValidPhoneNumber(String input) {

        return input.matches("\\d{10}") || input.matches("\\d{12}");
    }
    public String normalizPhoneNumber(String phoneNumber) {
        String normalizedNumber = phoneNumber.replaceAll("[^0-9]", "");

        if (isValidPhoneNumber(normalizedNumber)) {
            if (normalizedNumber.startsWith("09")) {
                normalizedNumber = "251" + normalizedNumber.substring(1);
            } else if (normalizedNumber.startsWith("251")) {
            } else {

                throw new IllegalArgumentException("Unsupported phone number format");
            }
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }

        return normalizedNumber;
    }
    public boolean isTokenExpired(String jwtToken) {
        try {
            Claims claims = jwtService.verify(jwtToken);
            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            return expirationDate != null && expirationDate.before(now);
        }

        catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return true;
        }
        catch (Exception e) {
            return true;
        }
    }


}












