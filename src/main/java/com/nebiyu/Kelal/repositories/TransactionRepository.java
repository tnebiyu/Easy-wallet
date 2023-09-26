package com.nebiyu.Kelal.repositories;

import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {

    List<TransactionModel> findBySenderOrReceiver(User sender, User receiver);
}
