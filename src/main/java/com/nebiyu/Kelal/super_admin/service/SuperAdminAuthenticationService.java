package com.nebiyu.Kelal.super_admin.service;

import com.nebiyu.Kelal.admin.admin_service.AdminService;
import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.request.RegisterRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.super_admin.model.SuperAdminModel;
import com.nebiyu.Kelal.super_admin.super_admin_repo.SuperAdminRepo;
import io.jsonwebtoken.Claims;
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
public class SuperAdminAuthenticationService {

  private final  SuperAdminRepo superAdminRepo;
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
  public AuthenticationResponse createAdminAccount(AuthenticationRequest request, RegisterRequest adminToCreate, String jwtToken) {
    try {
      Optional<SuperAdminModel> superAdminExists = superAdminRepo.findByEmail(request.getEmail());
      Claims claims = jwtService.verify(jwtToken);
      String senderEmail =(String) claims.get("email");
      if (superAdminExists.isEmpty()){
        return AuthenticationResponse.builder().error(true).error_msg("super admin is not available with this account").build();
      }
      SuperAdminModel sAdmin = superAdminExists.get();
      if (!passwordEncoder.matches(senderEmail, sAdmin.getPassword())) {
        return AuthenticationResponse.builder().error(true).error_msg("password is incorrect. Permission denied").build();
      }
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
              request.getEmail(),
              request.getPassword()));


      adminService.creatAdmin(adminToCreate);


      return AuthenticationResponse.builder().error(false).error_msg("Admin account created successfully").build();
    } catch (Exception e) {
      return AuthenticationResponse.builder().error(true).error_msg(e.getMessage()).build();
    }
  }



  private boolean isPasswordComplex(String password) {
    return password.length() >= 8 &&
            password.matches(".*[a-zA-Z].*") &&
            password.matches(".*\\d.*") &&
            password.matches(".*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>/?].*");
  }

}
