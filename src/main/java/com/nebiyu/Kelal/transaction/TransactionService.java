package com.nebiyu.Kelal.transaction;

import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.model.TransactionModel;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.TransactionRepository;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.request.TransferRequest;
import com.nebiyu.Kelal.response.TransferResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private TransactionRepository transactionRepository;
    @Transactional
    public ResponseEntity<TransferResponse> transferMoneyByEmail(TransferRequest request) {
        try {

            Optional<User> senderOptional = userRepository.findByEmail(request.getEmail());
            Optional<User> receiverOptional = userRepository.findByEmail(request.getEmail());


            if (senderOptional.isEmpty() || receiverOptional.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(TransferResponse.builder()
                                .error(true)
                                .error_msg("Sender or receiver not found")
                                .build());
            }

            User sender = senderOptional.get();
            User receiver = receiverOptional.get();

            BigDecimal amount =  request.getAmount();


            if (sender.getBalance().compareTo(amount) <0) {
                return ResponseEntity.badRequest()
                        .body(TransferResponse.builder()
                                .error(true)
                                .error_msg("Insufficient balance")
                                .build());
            }


            sender.setBalance(sender.getBalance().subtract(amount));


            receiver.setBalance(receiver.getBalance().add(amount));


            userRepository.save(sender);
            userRepository.save(receiver);


            TransactionModel transaction = new TransactionModel();
            transaction.setSender(sender);
            transaction.setReceiver(receiver);
            transaction.setAmount(amount);
            transaction.setTimestamp(new Date());
            transactionRepository.save(transaction);

            TransferResponse.UserData userData = TransferResponse.UserData.builder()
                    .access_token("")
                    .senderEmail(sender.getEmail())
                    .receiverEmail(receiver.getEmail())
                    .user_id(sender.getId())
                    .password(sender.getPassword())
                    .balance(sender.getBalance())

                    .build();

            TransferResponse.Data data = TransferResponse.Data.builder()
                    .user_data(userData)
                    .build();

            TransferResponse response = TransferResponse.builder()
                    .data(data)
                    .error(false)
                    .error_msg("")
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TransferResponse.builder()
                            .error(true)
                            .error_msg("Transfer failed: " + e.getMessage())
                            .build());
        }
    }
    private boolean isTokenValid(String token) {
        if (token.isEmpty()) {
            return false;
        }

       boolean  isValid=   jwtService.isTokenExpired1(token);

        return true;
    }

    // Implement token extraction logic here
    private String extractUsernameFromToken(String token) {
        // Implement token validation logic
        // Extract and return the username from the token
        return null;
    }
//    @Transactional
//    public boolean transferMoney(String senderUsername, String receiverUsername, BigDecimal amount) {
//
//
//        Optional<User> senderOptional = userRepository.findByFirstName(senderUsername);
//        Optional<User> receiverOptional = userRepository.findByFirstName(receiverUsername);
//
//        if (senderOptional.isEmpty() || receiverOptional.isEmpty()) {
//            return false;
//        }
//
//        User sender = senderOptional.get();
//        User receiver = receiverOptional.get();
//
//        BigDecimal senderBalance = sender.getBalance();
//
//        if (senderBalance.compareTo(amount) >= 0) {
//
//            sender.setBalance(senderBalance.subtract(amount));
//
//            BigDecimal receiverBalance = receiver.getBalance();
//            receiver.setBalance(receiverBalance.add(amount));
//
//
//            userRepository.save(sender);
//            userRepository.save(receiver);
//
//            TransactionModel transaction = new TransactionModel();
//            transaction.setSender(sender);
//            transaction.setReceiver(receiver);
//            transaction.setAmount(amount);
//            transaction.setTimestamp(new Date());
//            transactionRepository.save(transaction);
//
//            return true;
//        } else {
//            return false;
//        }
//    }


}

