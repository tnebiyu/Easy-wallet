package com.nebiyu.Kelal.admin.admin_service;
import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.dao.*;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.dto.Response;
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
public class AdminService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Async
  public  Response registerAdmin(RegisterRequest request){
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

            if (existingUser.isPresent()) {
                return Response.builder()
                        .error(true)
                        .error_msg("email already exists")
                        .build();
            }

            String password = request.getPassword();
            if (!isPasswordComplex(password)) {
                return Response.builder()
                        .error(true)
                        .error_msg("invalid_password")
                        .build();
            }

            User admin = User.builder()
                    .firstName(request.getFirstname())
                    .lastName(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .balance(BigDecimal.ZERO)
                    .build();
            userRepository.save(admin);
            var responseBuilder = Response.builder().error(false)
                    .error_msg("");
            var userData = Response.AuthorizationResponse.builder().firstName(request.getFirstname())
                    .lastName(request.getLastname()).email(request.getEmail())
                    .balance(BigDecimal.ZERO)
                    .build();

            var data = Response.Data.builder()
                    .authorizationResponse(userData).build();
            responseBuilder.data(data).build();


            return Response.builder().data(data).error(false).error_msg("").build();
        } catch (Exception e) {
            return Response.builder()
                    .error(true)
                    .error_msg(e.getMessage())
                    .build();
        }

    }
public Response changePassword(ChangePasswordRequest request, String jwtToken){
    try{
        Optional<User> userExist = userRepository.findByEmail(request.getEmail());
        if (userExist.isEmpty()) {
            return Response.builder().error(true)
                    .error_msg("user is not registered, please register").build();
        }
        Claims claims = jwtService.verify(jwtToken);
        String email =(String) claims.get("email");
        if (!Objects.equals(email, request.getEmail()) && isTokenExpired(jwtToken)) {

            return Response.builder().error(true).error_msg("User is not authenticated or token is expired").build();
        }
        User admin = userExist.get();
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            return Response.builder().error(true).error_msg("password is incorrect").build();
        }
        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(admin);
        var user_data = Response.ChangePassword.builder()
                .email(admin.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .newPassword(passwordEncoder.encode(request.getNewPassword()))
                .build();
        var data= Response.Data.builder()
                .changePassword(user_data).build();
        return Response.builder().error(false)
                .error_msg("").data(data).build();


    }
    catch (Exception e){

        return Response.builder().error(true).error_msg(e.toString()).build();

    }

}
    @Async
    public Response registerWithPhoneNumber(RegisterWithPhoneRequest request) {
        try {
            String normalizePhoneNumber = normalizePhoneNumber(request.getPhoneNumber());
            Optional<User> existingUser = userRepository.findByPhoneNumber(normalizePhoneNumber);
            if (!isValidPhoneNumber(request.getPhoneNumber())){
                return Response.builder().error(true)
                        .error_msg("Invalid phone number").build();
            }

            if (existingUser.isPresent()) {
                return Response.builder()
                        .error(true)
                        .error_msg("admin already registered")
                        .build();
            }

            String password = request.getPassword();
            if (!isPasswordComplex(password)) {
                return Response.builder()
                        .error(true)
                        .error_msg("invalid_password")
                        .build();
            }

            User user = User.builder()
                    .firstName(request.getFirstname())
                    .lastName(request.getLastname())
                    .phoneNumber(normalizePhoneNumber)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .balance(BigDecimal.ZERO)
                    .build();
            userRepository.save(user);
            var responseBuilder = Response.builder().error(false)
                    .error_msg("");
            var userData = Response.AuthorizationResponse.builder().firstName(request.getFirstname())
                    .lastName(request.getLastname())
                    .phoneNumber(request.getPhoneNumber())
                    .balance(BigDecimal.ZERO)
                    .build();

            var data = Response.Data.builder()
                    .authorizationResponse(userData).build();
            responseBuilder.data(data).build();


            return Response.builder().data(data).error(false).error_msg("").build();
        } catch (Exception e) {
            return Response.builder()
                    .error(true)
                    .error_msg(e.getMessage())
                    .build();
        }
    }
    public Response signInWithPhoneNumber(PhoneAuthRequest request){
        try {
            String normalizePhoneNumber = normalizePhoneNumber(request.getPhone());
            Optional<User> userExist = userRepository.findByPhoneNumber(normalizePhoneNumber);

            if (!isValidPhoneNumber(request.getPhone())){
                return Response.builder().error(true)
                        .error_msg("Invalid phone number").build();
            }



            if (userExist.isEmpty()) {

                return Response.builder().error(true)
                        .error_msg("admin is not registered, please register").build();
            }
            User user = userExist.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

                return Response.builder().error(true).error_msg("password is incorrect").build();
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getPhone(),
                    request.getPassword()));

            var jwtToken = jwtService.generateToken(user);
            var userData = Response.UserData.builder()
                    .user_id(user.getId())
                    .access_token(jwtToken)
                    .phoneNumber(user.getPhoneNumber())
                    .balance(user.getBalance())
                    .build();
            var data = Response.Data.builder()
                    .user_data(userData).build();
            return Response.builder().data(data).error_msg("").error(false).build();

        }
        catch (Exception e){
            return Response.builder().error(true).error_msg("Authentication Failed" + e.getMessage()).build();
        }
    }
    public Response refreshToken(RefreshTokenRequest refreshTokenRequest){
        try{
            String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
            User user = userRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
                var jwt = jwtService.generateToken(user);
                var refreshToken = Response.RefreshToken.builder().refreshToken(refreshTokenRequest.getToken()).token(jwt).build();
                var data = Response.Data.builder().refreshToken(refreshToken).build();
                return Response.builder().data(data).error(false).error_msg("").build();
            }
            else {
                return Response.builder().error(true).error_msg("token is invalid").build();
            }
        }
        catch (Exception e){
            return Response.builder().error(true).error_msg("Refresh token failed" + e.getMessage()).build();

        }

    }
    @Async
    public Response authenticateAdmin(AuthenticationRequest request) {
        try {
            Optional<User> adminExist = userRepository.findByEmail(request.getEmail());

            if (adminExist.isEmpty()) {
                return Response.builder().error(true)
                        .error_msg("admin is not registered, please register").build();
            }

            User admin = adminExist.get();
            if (admin.getRole() != Role.ADMIN){
                return Response.builder()
                        .error(true).error_msg("this email is not an admin").build();
            }
            if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                return Response.builder().error(true).error_msg("password is incorrect").build();
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
            var jwtToken = jwtService.generateToken(admin);

            var responseBuilder = Response.builder()
                    .error(false)
                    .error_msg("");


            var userData = Response.UserData.builder()
                    .user_id(admin.getId())
                    .access_token(jwtToken)
                    .email(admin.getEmail())
                    .balance(admin.getBalance())
                    .build();
            var data = Response.Data.builder()
                    .user_data(userData).build();
            responseBuilder.data(data).build();


            return responseBuilder.build();
        }
        catch (Exception e){
            return Response.builder().error(true).error_msg("Authentication Failed" + e.getMessage()).build();
        }
    }
    @Async
    public Response topUpUser(TopUpRequest request, String jwtToken){
        try{

            Optional<User> adminOptional = userRepository.findByEmail(request.getAdminEmail());
            Optional<User> userOptional = userRepository.findByEmail(request.getUserEmail());
            Claims claims = jwtService.verify(jwtToken);
            String adminEmail =(String) claims.get("email");
            if (!Objects.equals(adminEmail, request.getAdminEmail()) && isTokenExpired(jwtToken)) {
               // System.out.println("admin email " + adminEmail + "request admin email " + request.getAdminEmail() + "jwt token " + isTokenExpired(jwtToken) + jwtToken);

                return Response.builder().error(true).error_msg("admin is not authenticated or token is expired").build();
            }
            if (adminOptional.isEmpty()){
                return Response.builder().error(true).error_msg("account not found for super admin")
                        .build();

            }
            if (userOptional.isEmpty()){
                return Response.builder().error(true).error_msg("account not found for a user").build();
            }
            if (Objects.equals(adminOptional, userOptional)) {
                return Response.builder().error(true).error_msg("Sender and Receiver are the same").build();
            }
            User admin = adminOptional.get();
            User user = userOptional.get();
            if (admin.getRole() != Role.ADMIN){
                return Response.builder().error(true).error_msg("this account is not admin").build();
            }
            if (admin.getBalance().compareTo(request.getAmount()) <= 0) {
                return Response.builder().error(true)
                        .error_msg("no balance please recharge your account")
                        .build();

            }
            admin.setBalance(admin.getBalance().subtract(request.getAmount()));
            user.setBalance(user.getBalance().add(request.getAmount()));


            if (isTokenExpired(jwtToken)) {
                return Response.builder().error(true).error_msg("token expired please login again").build();
            }
            userRepository.save(admin);
            userRepository.save(user);

            var topUpData = Response.TopUpResponse.builder().superAdminEmail(
                            admin.getEmail()
                    ).userEmail(user.getEmail())
                    .newBalance(user.getBalance())
                    .status("success").build();
            var Data = Response.Data.builder().topUpResponse(topUpData)
                    .build();
            return Response.builder().error(false)
                    .error_msg("").data(Data).build();
        }
        catch (Exception e){
            return Response.builder().error(true).error_msg(e.toString()).build();
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
    private boolean isValidPhoneNumber(String input) {

        return input.matches("\\d{10}") || input.matches("\\d{12}");
    }
    public String normalizePhoneNumber(String phoneNumber) {
        String normalizedNumber = phoneNumber.replaceAll("[^0-9]", "");

        if (isValidPhoneNumber(normalizedNumber)) {
            if (normalizedNumber.startsWith("09")) {
                normalizedNumber = "251" + normalizedNumber.substring(1);
            } else if (normalizedNumber.startsWith("9")) {
                normalizedNumber = "251" + normalizedNumber;
            } else if (normalizedNumber.startsWith("251")) {
            } else {

                throw new IllegalArgumentException("Unsupported phone number format");
            }
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }

        return normalizedNumber;
    }






}
