package com.nebiyu.Kelal.repositories;

import com.nebiyu.Kelal.model.PhoneUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepoPhoneNumber extends JpaRepository<PhoneUserModel, Long> {
    Optional<PhoneUserModel> findByPhoneNumber(String phoneNumber);
    Optional<PhoneUserModel> findById(Long id);
}
