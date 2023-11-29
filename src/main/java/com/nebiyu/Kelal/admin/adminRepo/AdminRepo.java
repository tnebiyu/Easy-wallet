package com.nebiyu.Kelal.admin.adminRepo;

import com.nebiyu.Kelal.admin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    @Query("SELECT u FROM Admin u WHERE u.email = :email")
    Optional<Admin> findByEmail(@Param("email") String email);
    Optional<Admin> findById( Long id);
}
