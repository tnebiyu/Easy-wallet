package com.nebiyu.Kelal.admin.admin_service;

import com.nebiyu.Kelal.admin.adminRepo.AdminRepo;
import com.nebiyu.Kelal.admin.model.Admin;
import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.request.RegisterRequest;
import com.nebiyu.Kelal.request.TopUpRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.super_admin.model.SuperAdminModel;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
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
//    @Async
//    public AuthorizationResponse createAdmin(Admin admin) {
//        try {
//Optional<Admin> userExists = adminRepo.findByEmail(admin.getEmail());
//if (userExists.isPresent()){
//    return AuthorizationResponse.builder().error(true).error_msg("user already exists").build();
//}
//var admin1 = Admin.builder().email(admin.getEmail()).
//        firstName(admin.getFirstName()).lastName(admin.getLastName())
//                .balance(BigDecimal.ZERO).role(Role.ADMIN).build();
//            adminRepo.save(admin1);
//            var adminData = AuthorizationResponse.UserData.builder()
//                    .email(admin.getEmail()).balance(BigDecimal.ZERO)
//                    .firstName(admin.getFirstName())
//                    .lastName(admin.getLastName()).build();
//            var data = AuthorizationResponse.Data.builder()
//                    .user_data(adminData).build();
//
//            return   AuthorizationResponse.builder()
//                    .error(false).error_msg("")
//                    .data(data).build();
//
//        } catch (Exception e) {
//
//            return AuthorizationResponse.builder()
//                    .error(true).error_msg("error occurred " + e.getMessage()).build();
//        }
//    }
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
    @Async
    public AuthenticationResponse topUpUser(TopUpRequest request, String jwtToken){
        try{

            Optional<Admin> superAdmin = adminRepo.findByEmail(request.getAdminEmail());
            Optional<User> userOptional = userRepository.findByEmail(request.getUserEmail());
            Claims claims = jwtService.verify(jwtToken);
            String superAdminEmail =(String) claims.get("email");
            if (superAdmin.isEmpty()){
                return AuthenticationResponse.builder().error(true).error_msg("account not found for super admin")
                        .build();

            }
            if (userOptional.isEmpty()){
                return AuthenticationResponse.builder().error(true).error_msg("account not found for a user").build();
            }
            Admin admin = superAdmin.get();
            if (!passwordEncoder.matches(superAdminEmail, admin.getPassword()) && isTokenExpired(jwtToken)) {
                return AuthenticationResponse.builder().error(true).error_msg("password is incorrect. Permission is denied").build();
            }
            User user = userOptional.get();
            BigDecimal newBalance =   user.getBalance().add(request.getAmount());

            var topUpData = AuthenticationResponse.TopUpResponse.builder().superAdminEmail(
                            admin.getEmail()
                    ).userEmail(user.getEmail())
                    .newBalance(newBalance)
                    .status("success").build();
            var Data = AuthenticationResponse.Data.builder().topUpResponse(topUpData)
                    .build();
            return AuthenticationResponse.builder().error(false)
                    .error_msg("").data(Data).build();
        }
        catch (Exception e){
            return AuthenticationResponse.builder().error(true).error_msg(e.toString()).build();
        }



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
    private boolean isPasswordComplex(String password) {
        return password.length() >= 8 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>/?].*");
    }





}
