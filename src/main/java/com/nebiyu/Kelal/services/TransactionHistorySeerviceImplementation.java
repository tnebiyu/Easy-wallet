//package com.nebiyu.Kelal.services;
//
//import com.nebiyu.Kelal.model.TransactionModel;
//
//import com.nebiyu.Kelal.repositories.TransactionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class TransactionHistorySeerviceImplementation implements TransactionHistorySeervice{
//@Autowired
//private TransactionRepository transactionRepository;
//    @Override
//    public List<TransactionModel> getAllTransactions() {
//        return transactionRepository.findAll();
//    }
//
////    @Override
////    public TransactionModel saveTransaction(TransactionResponse response) {
////        return transactionRepository.saveAndFlush(response);
////    }
//
//    @Override
//    public TransactionModel getSingleTransaction(Long id) {
//        Optional<TransactionModel> transaction = transactionRepository.findBySender_Id(id);
//
//       if (transaction.isPresent()){
//           System.out.println(transaction.get());
//           return  transaction.get();
//       }
//       throw new RuntimeException("Transaction not found");
//    }
//}
