package com.nebiyu.Kelal.super_admin.service;

import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.dto.request.*;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;

import com.nebiyu.Kelal.dto.response.AuthenticationResponse;
import com.nebiyu.Kelal.dto.response.AuthorizationResponse;
import com.nebiyu.Kelal.dto.response.ChangePasswordResponse;
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
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuperAdminAuthenticationService {

  private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Async
  public  AuthorizationResponse registerSuperAdmin(RegisterRequest request){
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

        User superAdmin = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.SUPERADMIN)
                .balance(BigDecimal.ZERO)
                .build();
        userRepository.save(superAdmin);
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
      Optional<User> superAdminExist = userRepository.findByEmail(request.getEmail());

      if (superAdminExist.isEmpty()) {
        return AuthenticationResponse.builder().error(true)
                .error_msg("super admin is not registered, please register").build();
      }
      User superAdmin = superAdminExist.get();

      if (!passwordEncoder.matches(request.getPassword(), superAdmin.getPassword())) {
        return AuthenticationResponse.builder().error(true).error_msg("password is incorrect").build();
      }
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
              request.getEmail(),
              request.getPassword()));
      var jwtToken = jwtService.generateToken(superAdmin);

      var responseBuilder = AuthenticationResponse.builder()
              .error(false)
              .error_msg("");


      var userData = AuthenticationResponse.UserData.builder()
              .user_id(superAdmin.getId())
              .access_token(jwtToken)
              .email(superAdmin.getEmail())
              .balance(superAdmin.getBalance())
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
  public AuthenticationResponse createAdminAccount(SadminCreateAdminRequest request, String jwtToken) {
    try {
      Optional<User> superAdminExists = userRepository.findByEmail(request.getSuperAdminEmail());
      Optional<User> adminExists = userRepository.findByEmail(request.getAdminEmail());
      Claims claims = jwtService.verify(jwtToken);

      String email =(String) claims.get("email");
      if (!Objects.equals(email, request.getSuperAdminEmail()) && isTokenExpired(jwtToken)) {

        return AuthenticationResponse.builder().error(true).error_msg("Super admin is not authenticated or token is expired").build();
      }
      if (superAdminExists.isEmpty()){
        return AuthenticationResponse.builder().error(true).error_msg("super admin is not available with this account").build();
      }
      if (adminExists.isPresent()){
        return AuthenticationResponse.builder().error(true).error_msg("admin is already registered").build();

      }

      if (Objects.equals(superAdminExists, adminExists)) {
        return AuthenticationResponse.builder().error(true).error_msg("admin and super admin are the same").build();
      }
      User superAdmin = superAdminExists.get();
      if (superAdmin.getRole() != Role.SUPERADMIN){
        return AuthenticationResponse.builder().error(true).error_msg("this account is not super admin").build();
      }
      if (!isPasswordComplex(request.getAdminPassword())){
        return AuthenticationResponse.builder().error(true).error_msg("password is not strong").build();

      }

      User admin = User.builder().email(request.getAdminEmail())
                      .password(passwordEncoder.encode(request.getAdminPassword()))
                              .firstName(request.getAdminFirstName())
                                      .lastName(request.getAdminLastName())
                                              .balance(BigDecimal.ZERO)
              .role(Role.ADMIN).build();
      userRepository.save(admin);
      var adminCreated =AuthenticationResponse.AdminCreated.builder()
              .isAdminCreated(true)
              .adminEmail(request.getAdminEmail())
              .adminPassword(request.getAdminPassword())
              .createdBy(request.getSuperAdminEmail())
              .adminFirstName(request.getAdminFirstName())
              .adminLastName(request.getAdminLastName())
              .timeStamp(new Date())
              .build();
      var data = AuthenticationResponse.Data.builder()
              .adminCreated(adminCreated)
              .build();


      return AuthenticationResponse.builder().error(false).error_msg("Admin account created successfully").data(data).build();
    } catch (Exception e) {
      return AuthenticationResponse.builder().error(true).error_msg(e.getMessage()).build();
    }
  }
  public ChangePasswordResponse changePassword(ChangePasswordRequest request, String jwtToken){
    try{
      Optional<User> userExist = userRepository.findByEmail(request.getEmail());
      if (userExist.isEmpty()) {
        return ChangePasswordResponse.builder().error(true)
                .error_msg("user is not registered, please register").build();
      }
      User sAdmin = userExist.get();
      if (sAdmin.getRole() != Role.SUPERADMIN){
        return ChangePasswordResponse.builder().error(true).error_msg("this account is not a super admin")
                .build();
      }

      Claims claims = jwtService.verify(jwtToken);
      String email =(String) claims.get("email");
      if (!Objects.equals(email, request.getEmail()) && isTokenExpired(jwtToken)) {

        return ChangePasswordResponse.builder().error(true).error_msg("User is not authenticated or token is expired").build();
      }
      if (passwordEncoder.matches(request.getNewPassword(), sAdmin.getPassword())){
        return ChangePasswordResponse.builder().error(true).error_msg("the new password is same as the old one").build();
      }
      if (!passwordEncoder.matches(request.getPassword(), sAdmin.getPassword())){
        return ChangePasswordResponse.builder().error(true).error_msg("password is incorrect").build();
      }

      sAdmin.setPassword(passwordEncoder.encode(request.getNewPassword()));
      userRepository.save(sAdmin);
      var user_data = ChangePasswordResponse.ChangePassword.builder()
              .email(sAdmin.getEmail())
              .newPassword(request.getNewPassword())
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
  @Async
  public AuthenticationResponse topUpUser(TopUpRequest request, String jwtToken){
      try{

        Optional<User> superAdminOpitonal = userRepository.findByEmail(request.getAdminEmail());
        Optional<User> userOptional = userRepository.findByEmail(request.getUserEmail());
        Claims claims = jwtService.verify(jwtToken);
        String superAdminEmail =(String) claims.get("email");
        if (!Objects.equals(superAdminEmail, request.getAdminEmail()) && isTokenExpired(jwtToken)) {

          return AuthenticationResponse.builder().error(true).error_msg("super admin is not authenticated or token is expired").build();
        }
        if (superAdminOpitonal.isEmpty()){
          return AuthenticationResponse.builder().error(true).error_msg("account not found for super admin")
                  .build();

        }
        if (userOptional.isEmpty()){
          return AuthenticationResponse.builder().error(true).error_msg("account not found for a user").build();
        }
        User superAdmin = superAdminOpitonal.get();
        if (Objects.equals(superAdminOpitonal, userOptional)) {
          return AuthenticationResponse.builder().error(true).error_msg("Super admin and Receiver are the same").build();
        }
        if ( isTokenExpired(jwtToken)) {
          return AuthenticationResponse.builder().error(true).error_msg("token expired please login again").build();
        }
        if (superAdmin.getRole() != Role.SUPERADMIN){
          return AuthenticationResponse.builder().error(true).error_msg("this account is not super admin").build();
        }
        if (superAdmin.getBalance().compareTo(request.getAmount()) <= 0) {
          return AuthenticationResponse.builder().error(true).error_msg("no balance please recharge your account").build();

        }
        User user = userOptional.get();
        superAdmin.setBalance(superAdmin.getBalance().subtract(request.getAmount()));
        user.setBalance(user.getBalance().add(request.getAmount()));

     BigDecimal newBalance =   user.getBalance().add(request.getAmount());
     userRepository.save(user);
     userRepository.save(superAdmin);

        var topUpData = AuthenticationResponse.TopUpResponse.builder().superAdminEmail(
                superAdmin.getEmail()
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
