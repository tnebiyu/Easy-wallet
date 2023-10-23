package com.nebiyu.Kelal.repositories;
import com.nebiyu.Kelal.model.TransactionModel;
import com.nebiyu.Kelal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
    @Query("SELECT t FROM TransactionModel t WHERE t.sender.id = :userId OR t.receiver.id = :userId")
    List<TransactionModel> findAllTransactionsForUser(@Param("userId") Long userId);

//    Optional<User> findByEmail(@Param("email") String email);
//    List<TransactionModel> findBySenderOrReceiver(Optional<User> sender, Optional<User> receiver);
//    List<TransactionModel> findByReceiver_Id(Long receiverId);
//    Optional<TransactionModel> findBySenderEmail(String email);
//   List<TransactionModel> findBySenderId(long id);
//    List<TransactionModel> findBySender(String senderEmail);
//    List<TransactionModel> findByReceiver(String receiverEmail);
}
