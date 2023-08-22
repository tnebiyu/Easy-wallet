package com.nebiyu.Kelal.services.auth;

import com.nebiyu.Kelal.dto.SignUpDTO;
import com.nebiyu.Kelal.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {


    UserDTO signUp(SignUpDTO signUpDTO);
}
