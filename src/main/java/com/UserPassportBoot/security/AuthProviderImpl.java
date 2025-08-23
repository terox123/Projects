package com.UserPassportBoot.security;

import com.UserPassportBoot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final UserService userService;
@Autowired
    public AuthProviderImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
String userName = authentication.getName();
UserDetails userDetails = userService.loadUserByUsername(userName);
String password = authentication.getCredentials().toString();
if(!password.equals(userDetails.getPassword())){
    throw new BadCredentialsException("Incorrect password");
}
return new UsernamePasswordAuthenticationToken(userDetails, password, Collections.emptyList());


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
