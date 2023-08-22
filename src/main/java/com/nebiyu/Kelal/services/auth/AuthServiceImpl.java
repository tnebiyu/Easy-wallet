package com.nebiyu.Kelal.services.auth;

import com.nebiyu.Kelal.dto.SignUpDTO;
import com.nebiyu.Kelal.dto.UserDTO;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthServiceImpl implements AuthService {
   @Autowired
    private JpaRepository userRepository;
    @Override
    public UserDTO signUp(SignUpDTO signUpDTO) {
        User user = new User();
        user.setFirstName(signUpDTO.getFirstName());
        user.setLastName(signUpDTO.getLastName());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signUpDTO.getPassword()));
       User createdUser = (User) userRepository.save(user);
       UserDTO userDTO = new UserDTO();
         userDTO.setFirstName(createdUser.getFirstName());
            userDTO.setLastName(createdUser.getLastName());
            userDTO.setEmail(createdUser.getEmail());


        return userDTO;
    }
}
