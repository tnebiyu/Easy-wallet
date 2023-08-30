package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.configuration.JwtConfig;
import com.nebiyu.Kelal.services.auth.AuthService;
import com.nebiyu.Kelal.services.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (authService.authenticateUser(username, password)) {
            long expirationMillis= 24 * 60 * 60 * 1000;
          //  String accessToken = jwtConfig.generateToken(username);
            String accessToken = jwtTokenProvider.generateToken(username, expirationMillis);

            Map<String, Object> userData = new HashMap<>();
            userData.put("access_token", accessToken);
            // Populate other user data fields as needed

            Map<String, Object> data = new HashMap<>();
            data.put("user_data", userData);

            Map<String, Object> response = new HashMap<>();
            response.put("data", data);
            response.put("error", false);
            response.put("error_msg", "");

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", true);
            response.put("error_msg", "Invalid credentials");

            return ResponseEntity.ok(response);
        }
    }
}

//
//import com.fasterxml.jackson.core.io.JsonEOFException;
//import com.nebiyu.Kelal.dto.AuthenticationDTO;
//import com.nebiyu.Kelal.dto.AuthenticationResponse;
//import com.nebiyu.Kelal.services.auth.AuthService;
//import com.nebiyu.Kelal.services.jwt.UserDetailServiceImpl;
//import com.nebiyu.Kelal.services.util.JwtUtil;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//public class AuthenticationController {
//@Autowired
//private AuthService authService;
//@Autowired
//private AuthenticationManager authenticationManager;
//@Autowired
//private AuthenticationDTO authenticationDTO;
//@Autowired
//private UserDetailServiceImpl userDetailService;
//@Autowired
//private JwtUtil jwtUtil;
//@PostMapping("/authenticate")
//public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationDTO authenticationDTO, HttpServletResponse response) throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException, JsonEOFException {
//try{
//    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword()));
//}
//catch (BadCredentialsException e){
//    throw new BadCredentialsException("Incorrect username or password", e);
//}
//catch (DisabledException disabledException){
//   response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not Activated");
//   return null;
//}
//final UserDetails userDetails =  userDetailService.loadUserByUsername(authenticationDTO.getEmail());
//final String jwt = jwtUtil.generateToken(userDetails.getUsername());
//return new AuthenticationResponse(jwt);
//
//}
//
//
//}
