package com.nebiyu.Kelal.controllers;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.nebiyu.Kelal.dto.AuthenticationDTO;
import com.nebiyu.Kelal.dto.AuthenticationResponse;
import com.nebiyu.Kelal.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AuthenticationController {
@Autowired
private AuthService authService;
@Autowired
private AuthenticationManager authenticationManager;
public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationDTO authenticationDTO) throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException, JsonEOFException {
try{
    authenticationManager.authenticate(authenticationDTO.getEmail(), authenticationDTO.getPassword());
}
catch (BadCredentialsException e){
    throw new BadCredentialsException("Incorrect username or password", e);
}
catch (DisabledException e){
    throw new DisabledException("User is disabled", e);
}
    return null;
}


}
