package com.nebiyu.Kelal.services.auth;
import com.nebiyu.Kelal.dto.SignUpDTO;
import com.nebiyu.Kelal.dto.UserDTO;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.services.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
   @Autowired
    private JpaRepository jpaRepository;
   private UserRepository userRepository;
   @Autowired
   private JwtTokenProvider jwtTokenProvider;

    @Override
    public String generateToken(String firstName) {
         long expirationMillis= 24 * 60 * 60 * 1000;
        return jwtTokenProvider.generateToken(firstName,expirationMillis );
    }

    @Override
    public boolean authenticateUser(String firstName, String password) {
        User user = userRepository.findByFirstName(firstName);
        return user != null && user.getPassword().equals(password);
    }
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
