package com.nebiyu.Kelal.services;

import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.request.ChangePasswordRequest;
import com.nebiyu.Kelal.request.RegisterRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.response.ChangePasswordResponse;
import com.nebiyu.Kelal.response.TransferResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.CharArrayReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

@Async
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
            var responseBuilder = AuthorizationResponse.builder().error(false)
                    .error_msg("");
          var userData = AuthorizationResponse.UserData.builder().firstName(request.getFirstname())
                  .lastName(request.getLastname()).email(request.getEmail())
                .balance(BigDecimal.ZERO)
                  .build();

          var data = AuthorizationResponse.Data.builder()
                  .user_data(userData).build();
          responseBuilder.data(data).build();


            return AuthorizationResponse.builder().data(data).error(false).error_msg("").build();
        } catch (Exception e) {
            return AuthorizationResponse.builder()
                    .error(true)
                    .error_msg(e.getMessage())
                    .build();
        }
    }
    @Async
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
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
                    .balance(user.getBalance())
                   .build();
                       var data = AuthenticationResponse.Data.builder()
                    .user_data(userData).build();
            responseBuilder.data(data).build();


            return responseBuilder.build();
        }
        catch (Exception e){
            return AuthenticationResponse.builder().error(true).error_msg("Authentication Failed" + e.getMessage()).build();
        }
    }
    public ChangePasswordResponse changePassword(ChangePasswordRequest request, String jwtToken){
    try{
        Optional<User> userExist = userRepository.findByEmail(request.getEmail());
        if (userExist.isEmpty()) {
            return ChangePasswordResponse.builder().error(true)
                    .error_msg("user is not registered, please register").build();
        }
        Claims claims = jwtService.verify(jwtToken);
        String email =(String) claims.get("email");
        if (!Objects.equals(email, request.getEmail()) && isTokenExpired(jwtToken)) {

            return ChangePasswordResponse.builder().error(true).error_msg("User is not authenticated or token is expired").build();
        }
        User user = userExist.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ChangePasswordResponse.builder().error(true).error_msg("password is incorrect").build();
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        var user_data = ChangePasswordResponse.ChangePassword.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .newPassword(passwordEncoder.encode(request.getNewPassword()))
                .build();
        var data= ChangePasswordResponse.Data.builder()
                .changePassword(user_data).build();
        return ChangePasswordResponse.builder().error(false)
                .error_msg("").data(data).build();


    }
    catch (Exception e){

return ChangePasswordResponse.builder().error(true).error_msg(e.toString()).build();

    }

    }



    private boolean isPasswordComplex(String password) {
        return password.length() >= 8 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>/?].*");
    }
    public boolean isTokenExpired(String jwtToken) {
        try {
            Claims claims = jwtService.verify(jwtToken);
            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            return expirationDate != null && expirationDate.before(now);
        }

        catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return true;
        }
        catch (Exception e) {
            return true;
        }
    }







}

