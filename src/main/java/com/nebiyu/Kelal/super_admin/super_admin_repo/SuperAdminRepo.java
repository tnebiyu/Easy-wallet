package com.nebiyu.Kelal.super_admin.super_admin_repo;

import com.nebiyu.Kelal.super_admin.model.SuperAdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SuperAdminRepo extends JpaRepository<SuperAdminModel, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<SuperAdminModel> findByEmail(@Param("email") String email);

    Optional<SuperAdminModel> findById( Long id);
}
