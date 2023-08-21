package com.nebiyu.Kelal.repositories;

import com.nebiyu.Kelal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
