//package com.nebiyu.Kelal.services;
//import com.nebiyu.Kelal.configuration.JWTService;
//import com.nebiyu.Kelal.model.TransactionModel;
//import com.nebiyu.Kelal.model.User;
//import com.nebiyu.Kelal.repositories.TransactionRepository;
//import com.nebiyu.Kelal.repositories.UserRepository;
//import com.nebiyu.Kelal.request.TransactionHistoryRequest;
//import com.nebiyu.Kelal.response.TransactionHistoryResponse;
//import io.jsonwebtoken.Claims;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//
//@Service
//public class TransactionHistory {
//
//    @Autowired
//    JWTService jwtService;
//
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    public TransactionHistoryResponse getTransactionHistory(TransactionHistoryRequest request)  {
//        try{
//
//
////            Optional<User> userId = userRepository.findById(request.getId());
////            if (userId.isPresent()) {
////                User user = userId.get();
////                System.out.println("User found: " + user);
////            } else {
////                System.out.println("User not found for id: " + request.getId());
////                return TransactionHistoryResponse.builder().error(true).error_msg("user not found").build();
////            }
////            if (userId.isEmpty()){
////                return TransactionHistoryResponse.builder().error(true).error_msg("user not found").build();
////            }
//
//            List<TransactionModel> transactions = transactionRepository.findBySenderId(request.getId());
//            List<TransactionModel> sentTransactions = new ArrayList<>();
//            for (TransactionModel transactionModel : transactions) {
//                sentTransactions = transactionRepository.findBySenderId(request.getId());
//                if (transactionModel.getSender().getId()== request.getId()){
//                    sentTransactions.add(transactionModel);
//                }
//            }
//
//
//            TransactionHistoryResponse.Data data = TransactionHistoryResponse.Data.builder()
//                    .userData(TransactionHistoryResponse.UserData.builder()
//                            .sent(populateTransactionDetails(sentTransactions))
//                            .build())
//                    .build();
//
//            return TransactionHistoryResponse.builder()
//                    .data(data)
//                    .error(false)
//                    .error_msg("")
//                    .build();
//        }
//        catch (Exception e){
//            return TransactionHistoryResponse.builder().error(true).error_msg("error occured " + e).build();
//        }
//
//    }
//
//    private List<TransactionHistoryResponse.TransactionDetails> populateTransactionDetails(List<TransactionModel> transactions) {
//        List<TransactionHistoryResponse.TransactionDetails> transactionDetails = new ArrayList<>();
//        for (TransactionModel transaction : transactions) {
//            TransactionHistoryResponse.TransactionDetails details = TransactionHistoryResponse.TransactionDetails.builder()
//                    .receiverEmail(transaction.getReceiver())
//                    .sentTime(transaction.getTimestamp().toString())
//                    .balance(transaction.getAmount())
//                    .build();
//            transactionDetails.add(details);
//        }
//        return transactionDetails;
//    }
//
//}
