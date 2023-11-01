package com.nebiyu.Kelal.admin.admin_service;

import com.nebiyu.Kelal.admin.adminRepo.AdminRepo;
import com.nebiyu.Kelal.admin.model.Admin;
import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.request.RegisterRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.super_admin.model.SuperAdminModel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Async
  public  AuthorizationResponse registerAdmin(RegisterRequest request){
        try {
            Optional<Admin> existingUser = adminRepo.findByEmail(request.getEmail());

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

            var admin = Admin.builder()
                    .firstName(request.getFirstname())
                    .lastName(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .balance(BigDecimal.ZERO)
                    .build();
            adminRepo.save(admin);
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
    public AuthorizationResponse creatAdmin(RegisterRequest request) {
        try {
Optional<Admin> userExists = adminRepo.findByEmail(request.getEmail());
if (userExists.isPresent()){
    return AuthorizationResponse.builder().error(true).error_msg("user already exists").build();
}
var admin = Admin.builder().email(request.getEmail()).
        firstName(request.getFirstname()).lastName(request.getLastname())
                .balance(BigDecimal.ZERO).role(Role.ADMIN).build();
            adminRepo.save(admin);
            var adminData = AuthorizationResponse.UserData.builder()
                    .email(request.getEmail()).balance(BigDecimal.ZERO)
                    .firstName(request.getFirstname())
                    .lastName(request.getLastname()).build();
            var data = AuthorizationResponse.Data.builder()
                    .user_data(adminData).build();

            return   AuthorizationResponse.builder()
                    .error(false).error_msg("")
                    .data(data).build();

        } catch (Exception e) {

            return AuthorizationResponse.builder()
                    .error(true).error_msg("error occurred " + e.getMessage()).build();
        }
    }
    @Async
    public AuthenticationResponse authenticateAdmin(AuthenticationRequest request) {
        try {
            Optional<Admin> adminExist = adminRepo.findByEmail(request.getEmail());


            if (adminExist.isEmpty()) {
                return AuthenticationResponse.builder().error(true)
                        .error_msg(" admin is not registered, please register").build();
            }
            Admin admin = adminExist.get();
            if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                return AuthenticationResponse.builder().error(true).error_msg("password is incorrect").build();
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
            var jwtToken = jwtService.generateToken(admin);

            var responseBuilder = AuthenticationResponse.builder()
                    .error(false)
                    .error_msg("");


            var userData = AuthenticationResponse.UserData.builder()
                    .user_id(admin.getId())
                    .access_token(jwtToken)
                    .email(admin.getEmail())
                    .balance(admin.getBalance())
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
    private boolean isPasswordComplex(String password) {
        return password.length() >= 8 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>/?].*");
    }





}
