package com.nebiyu.Kelal.services.auth;

import com.nebiyu.Kelal.configuration.JwtConfig;
import com.nebiyu.Kelal.dto.SignUpDTO;
import com.nebiyu.Kelal.dto.UserDTO;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    String generateToken(String username);
    boolean authenticateUser(String username, String password);
    UserDTO signUp(SignUpDTO signUpDTO);
}
