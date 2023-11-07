package com.nebiyu.Kelal.repositories;

import com.nebiyu.Kelal.model.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findByPhoneNumber(String phoneNumber);
}
