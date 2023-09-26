package com.nebiyu.Kelal.repositories;

import com.nebiyu.Kelal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByPassword( @Param("password") String password);
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    Optional<User> findByFirstName(String firstName);
    Optional<User> findById(@Param("id") long id);

}
