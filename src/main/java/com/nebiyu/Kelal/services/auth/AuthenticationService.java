package com.nebiyu.Kelal.services.auth;

import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.controllers.RegisterRequest;
import com.nebiyu.Kelal.dto.AuthenticationResponse;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;



    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder().firstName(request.getFirstname()).lastName(request.getLastname()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder().jwtToken(jwtToken).build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().jwtToken(jwtToken).build();
    }
}
