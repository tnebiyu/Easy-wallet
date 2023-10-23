package com.nebiyu.Kelal.services;

import com.nebiyu.Kelal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServicePerUser {
    @Autowired
    UserRepository userRepository;


//    public TransactionPerUserResponse getTransactionOfUser(EmailRequest request){
//        try{
//
//        }
//        catch (Exception e){
//            return TransactionPerUserResponse.builder().error_msg(e.toString()).error(true).build();
//        }
//
//    }
}
