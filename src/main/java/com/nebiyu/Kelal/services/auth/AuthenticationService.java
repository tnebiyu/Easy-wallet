package com.nebiyu.Kelal.services.auth;

import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.controllers.RegisterRequest;
import com.nebiyu.Kelal.dto.AuthenticationResponse;
import com.nebiyu.Kelal.exception.RegistrationException;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<AuthenticationResponse> register(RegisterRequest request) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AuthenticationResponse.builder()
                                .error(true)
                                .error_msg("email already exists")
                                .build());
            }

            // Password complexity check
            String password = request.getPassword();
            if (!isPasswordComplex(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AuthenticationResponse.builder()
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
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);

            var responseBuilder = AuthenticationResponse.builder()
                    .error(false)
                    .error_msg("");

            if (!jwtToken.isEmpty()) {
                var userData = AuthenticationResponse.UserData.builder()
                        .access_token(jwtToken)
                        .email(user.getEmail())
                        .user_id(user.getId())
                        .build();

                var data = AuthenticationResponse.Data.builder()
                        .user_data(userData)
                        .build();

                responseBuilder.data(data);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(responseBuilder.build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthenticationResponse.builder()
                            .error(true)
                            .error_msg(e.getMessage())
                            .build());
        }
    }

    private boolean isPasswordComplex(String password) {
        return password.length() >= 8 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>/?].*");
    }





//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                request.getEmail(), request.getPassword()));
//        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
//        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse.builder().jwtToken(jwtToken).build();
//    }
}

