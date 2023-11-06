package com.nebiyu.Kelal.super_admin.service;

import com.nebiyu.Kelal.admin.admin_service.AdminService;
import com.nebiyu.Kelal.admin.model.Admin;
import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.request.RegisterRequest;
import com.nebiyu.Kelal.request.SadminCreateAdminRequest;
import com.nebiyu.Kelal.request.TopUpRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.super_admin.model.SuperAdminModel;
import com.nebiyu.Kelal.super_admin.super_admin_repo.SuperAdminRepo;
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
public class SuperAdminAuthenticationService {

  private final  SuperAdminRepo superAdminRepo;
  private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AdminService adminService;
    private final AuthenticationManager authenticationManager;

    @Async
  public  AuthorizationResponse registerSuperAdmin(RegisterRequest request){
      try {
        Optional<SuperAdminModel> existingUser = superAdminRepo.findByEmail(request.getEmail());

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

        var superAdmin = SuperAdminModel.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.SUPERADMIN)
                .balance(BigDecimal.ZERO)
                .build();
        superAdminRepo.save(superAdmin);
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
      Optional<SuperAdminModel> superAdminExist = superAdminRepo.findByEmail(request.getEmail());

      if (superAdminExist.isEmpty()) {
        return AuthenticationResponse.builder().error(true)
                .error_msg("super admin is not registered, please register").build();
      }
      SuperAdminModel superAdmin = superAdminExist.get();
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
      Optional<SuperAdminModel> superAdminExists = superAdminRepo.findByEmail(request.getSuperAdminEmail());
      Claims claims = jwtService.verify(jwtToken);
      String senderEmail =(String) claims.get("email");
      if (superAdminExists.isEmpty()){
        return AuthenticationResponse.builder().error(true).error_msg("super admin is not available with this account").build();
      }
      SuperAdminModel sAdmin = superAdminExists.get();
      if (!passwordEncoder.matches(senderEmail, sAdmin.getPassword()) && isTokenExpired(jwtToken)) {
        return AuthenticationResponse.builder().error(true).error_msg("password is incorrect. Permission denied").build();
      }
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
              request.getSuperAdminEmail(),
              request.getSuperAdminPassword()));
      Admin admin = Admin.builder().email(request.getAdminEmail())
                      .password(request.getAdminPassword())
                              .firstName(request.getAdminFirstName())
                                      .lastName(request.getAdminLastName())
                                              .balance(BigDecimal.ZERO)
              .role(Role.ADMIN)
                                                      .build();



      adminService.createAdmin(admin);


      return AuthenticationResponse.builder().error(false).error_msg("Admin account created successfully").build();
    } catch (Exception e) {
      return AuthenticationResponse.builder().error(true).error_msg(e.getMessage()).build();
    }
  }
  @Async
  public AuthenticationResponse topUpUser(TopUpRequest request, String jwtToken){
      try{

        Optional<SuperAdminModel> superAdmin = superAdminRepo.findByEmail(request.getAdminEmail());
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
        SuperAdminModel sAdmin = superAdmin.get();
        if (!passwordEncoder.matches(superAdminEmail, sAdmin.getPassword()) && isTokenExpired(jwtToken)) {
          return AuthenticationResponse.builder().error(true).error_msg("password is incorrect. Permission is denied").build();
        }
        User user = userOptional.get();
     BigDecimal newBalance =   user.getBalance().add(request.getAmount());

        var topUpData = AuthenticationResponse.TopUpResponse.builder().superAdminEmail(
                sAdmin.getEmail()
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
