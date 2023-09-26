package com.nebiyu.Kelal.services.auth;

import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.request.RegisterRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<AuthorizationResponse> register(RegisterRequest request) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

            if (existingUser.isPresent()) {
                return ResponseEntity.internalServerError()
                        .body(AuthorizationResponse.builder()
                                .error(true)
                                .error_msg("email already exists")
                                .build());
            }

            String password = request.getPassword();
            if (!isPasswordComplex(password)) {
                return ResponseEntity.badRequest()
                        .body(AuthorizationResponse.builder()
                                .error(true)
                                .error_msg("invalid_password")
                                .build());
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
            var jwtToken = jwtService.generateToken(user);

            var responseBuilder = AuthorizationResponse.builder()
                    .error(false)
                    .error_msg("");

            if (!jwtToken.isEmpty()) {
                var userData = AuthorizationResponse.UserData.builder()
                        .access_token(jwtToken)
                        .email(user.getEmail())
                        .user_id(user.getId())
                        .build();

                var data = AuthorizationResponse.Data.builder()
                        .user_data(userData)
                        .build();

                responseBuilder.data(data);
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBuilder.build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(AuthorizationResponse.builder()
                            .error(true)
                            .error_msg(e.getMessage())
                            .build());
        }
    }
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        try {
            Optional<User> userExist = userRepository.findByEmail(request.getEmail());

            if (userExist.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(AuthenticationResponse.builder()
                                .error(true)
                                .error_msg("user is not registered, please register")
                                .build());
            }
            User user = userExist.get();

//                String password = request.getPassword();
                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
                    return ResponseEntity.badRequest()
                            .body(AuthenticationResponse.builder()
                                    .error(true)
                                    .error_msg("password is incorrect")
                                    .build());

                }

                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
               // var user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()).orElseThrow();
                var jwtToken = jwtService.generateToken(user);
           // System.out.println("THIS IS THE PASSWORDDDDDDD" +  request.getPassword());
                var responseBuilder = AuthenticationResponse.builder()
                        .error(false)
                        .error_msg("");
                var userData = AuthenticationResponse.UserData.builder()
                        .user_id(user.getId())
                        .access_token(jwtToken)
                        .email(user.getEmail())
                        .balance(user.getBalance()).build();

                var data = AuthenticationResponse.Data.builder()
                        .user_data(userData).build();
                responseBuilder.data(data).build();

                return ResponseEntity.status(HttpStatus.OK)
                        .body(responseBuilder.build());



        } catch (Exception e) {
            return ResponseEntity.badRequest().body( AuthenticationResponse.builder()
                    .error(true)
                    .error_msg("Authentication failed: " + e.getMessage())
                    .build());
        }
    }


    private boolean isPasswordComplex(String password) {
        return password.length() >= 8 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>/?].*");
    }
//    private boolean isPasswordCorrect(String password) {
//        Optional<User> userOptional = userRepository.findByPassword( password);
//        return userOptional.isPresent();
//    }






}

