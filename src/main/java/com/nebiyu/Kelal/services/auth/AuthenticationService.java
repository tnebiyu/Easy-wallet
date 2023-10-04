package com.nebiyu.Kelal.services.auth;

import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.request.RegisterRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthorizationResponse register(RegisterRequest request) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

            if (existingUser.isPresent()) {
                return AuthorizationResponse.builder()
                        .error(true)
                        .error_msg("email already exists")
                        .build();
            }

            String password = request.getPassword();
            if (!isPasswordComplex(password)) {
                return AuthorizationResponse.builder()
                        .error(true)
                        .error_msg("invalid_password")
                        .build();
            }

            var user = User.builder()
                    .firstName(request.getFirstname())
                    .lastName(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .balance(BigDecimal.ZERO)
                    .build();
            userRepository.save(user);
//            var jwtToken = jwtService.generateToken(user);
// no need to generate token when user is registering
//            if (!jwtToken.isEmpty()) {
//                return AuthorizationResponse.builder()
//                        .error(false)
//                        .error_msg("")
//                        .data(AuthorizationResponse.Data.builder()
//                                .user_data(AuthorizationResponse.UserData.builder()
//                                        .access_token(jwtToken)
//                                        .email(user.getEmail())
//                                        .user_id(user.getId())
//                                        .build())
//                                .build())
//                        .build();
//            }

            return AuthorizationResponse.builder()
                    .error(true)
                    .error_msg("Unknown error occurred during registration")
                    .build();
        } catch (Exception e) {
            return AuthorizationResponse.builder()
                    .error(true)
                    .error_msg(e.getMessage())
                    .build();
        }
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        try {
            Optional<User> userExist = userRepository.findByEmail(request.getEmail());
            if (userExist.isEmpty()) {
                return AuthenticationResponse.builder().error(true)
                        .error_msg("user is not registered, please register").build();
            }
            User user = userExist.get();


            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return AuthenticationResponse.builder().error(true).error_msg("password is incorrect").build();
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
            var jwtToken = jwtService.generateToken(user);

            var responseBuilder = AuthenticationResponse.builder()
                    .error(false)
                    .error_msg("");
            var userData = AuthenticationResponse.UserData.builder()
                    .user_id(user.getId())
                    .access_token(jwtToken)
                    .email(user.getEmail())
                    .balance(user.getBalance()).build();
            Claims claims = jwtService.verify(jwtToken);
            String email =(String) claims.get("email");
            String password = (String) claims.get("password");
            System.out.println("THIS IS THE PAYLOAD" + email + ": " + password);

            var data = AuthenticationResponse.Data.builder()
                    .user_data(userData).build();
            responseBuilder.data(data).build();
            return AuthenticationResponse.builder().data(data).error_msg("").error(false).build();
        }
        catch (Exception e){
            return AuthenticationResponse.builder().error(true).error_msg("Authentication Failed" + e.getMessage()).build();
        }
    }



    private boolean isPasswordComplex(String password) {
        return password.length() >= 8 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>/?].*");
    }







}

