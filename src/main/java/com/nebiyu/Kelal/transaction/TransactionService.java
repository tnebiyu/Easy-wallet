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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
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
    @Autowired
    private TransferJwtTokenRequest tokenRequest;
    @Transactional
    public TransferResponse transferMoneyByEmail(TransferRequestWithEmail request, String jwtToken) {
        TransferResponse response = new TransferResponse();
        try {
             boolean isAuthenticated = jwtService.isTokenExpired(jwtToken);

            if (!isAuthenticated) {
                response.setError(true);
                response.setError_msg("User is not authenticated or token is expired");
                return response;
            }

         String userName = jwtService.extractUserName(jwtToken);
            Optional<User> senderOptional = userRepository.findByEmail(userName);
            Optional<User> receiverOptional = userRepository.findByEmail(request.getReceiverEmail());
            if (senderOptional.isEmpty() || receiverOptional.isEmpty()) {
                response.setError(true);
                response.setError_msg("Sender or receiver is not found");
                return response;
            }


            User sender = senderOptional.get();
            User receiver = receiverOptional.get();
            if (sender.getBalance().compareTo(request.getAmount()) <=0) {
//
                response.setError(true);
                response.setError_msg("insufficient amount please recharge your wallet");
            }
            userRepository.save(sender);
            userRepository.save(receiver);



            TransactionModel transaction = new TransactionModel();
            transaction.setSender(sender);
            transaction.setReceiver(receiver);
            transaction.setAmount(request.getAmount());
            transaction.setTimestamp(new Date());
            transactionRepository.save(transaction);
            TransferResponse.UserData userData = TransferResponse.UserData.builder()
                    .senderEmail(request.getSenderEmail())
                    .receiverEmail(request.getReceiverEmail())
                    .balance(sender.getBalance())
                    .build();

            TransferResponse.Data data = TransferResponse.Data.builder()
                    .user_data(userData)
                    .build();

            response.setData(data);
            response.setError(false);
            response.setError_msg("");

            return response;
        } catch (Exception e) {
            response.setError(true);
            response.setError_msg("Money transfer failed: " + e.getMessage());
            return response;
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







