package com.nebiyu.Kelal.transaction;

import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.model.TransactionModel;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.TransactionRepository;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.request.TransferJwtTokenRequest;
import com.nebiyu.Kelal.request.TransferRequestWithEmail;
import com.nebiyu.Kelal.response.TransferResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransferJwtTokenRequest tokenRequest;
    @Transactional
    @Async
    public TransferResponse transferMoneyByEmail(TransferRequestWithEmail request, String jwtToken) {

        try {

            Claims claims = jwtService.verify(jwtToken);
            String senderEmail =(String) claims.get("email");

            if (!Objects.equals(senderEmail, request.getSenderEmail())) {

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
//
               return TransferResponse.builder().error(true).error_msg("no balance please recharge your account").build();
            }
            sender.setBalance(sender.getBalance().subtract(request.getAmount()));
            receiver.setBalance(receiver.getBalance().add(request.getAmount()));

            userRepository.save(sender);
            userRepository.save(receiver);



            TransactionModel transaction = new TransactionModel();
            transaction.setSender(sender);
            transaction.setReceiver(receiver);
            transaction.setAmount(request.getAmount());
            transaction.setTimestamp(new Date());
            transactionRepository.save(transaction);

            List<TransactionModel> senderTransaction =sender.getSentTransactions();
            if (senderTransaction == null){
                senderTransaction = new ArrayList<>();
            }
            senderTransaction.add(transaction);
            sender.setSentTransactions(senderTransaction);
//            List<TransactionModel> receivedTransaction = receiver.getReceivedTransactions();
//            if (receivedTransaction == null){
//                receivedTransaction = new ArrayList<>();
//            }
//            receivedTransaction.add(transaction);
//            receiver.setSentTransactions(senderTransaction);

            TransferResponse.UserData userData = TransferResponse.UserData.builder()
                    .senderEmail(request.getSenderEmail())
                    .receiverEmail(request.getReceiverEmail())
                    .newBalance(sender.getBalance())
                    .build();

            TransferResponse.Data data = TransferResponse.Data.builder()
                    .user_data(userData)
                    .build();



            return TransferResponse.builder().error_msg("").error(false).data(data).build();
        } catch (Exception e) {
            return TransferResponse.builder().error(true).error_msg("Money transfer failed: " + e.getMessage()).build();
        }
    }
//    public boolean verifyTokenWithPassword(String jwtToken, String password) {
//        try {
//
//
//            // Extract the password from the JWT payload
//            String tokenPassword = (String) claims.get("password");
//
//            // Compare the extracted password with the provided password
//            return tokenPassword != null && tokenPassword.equals(password);
//        } catch (Exception e) {
//            // Handle any exceptions that may occur during token verification
//            return false;
//        }
//    }



}







