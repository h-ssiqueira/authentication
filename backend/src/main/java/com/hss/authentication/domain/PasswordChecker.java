package com.hss.authentication.domain;

import com.hss.authentication.commons.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordChecker extends AbstractUserDetailsAuthenticationProvider {

    private final SpringAuthService service;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordChecker(SpringAuthService service, @Qualifier("argonEncoder") PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var user = service.loadUserByUsernameWithSalt(authentication.getName());

        additionalAuthenticationChecks((UserDetails) authentication.getDetails(), new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials()));

        return super.createSuccessAuthentication(authentication.getName(), authentication, user);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return service.loadUserByUsernameWithSalt(username);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        var user = service.loadUserByUsernameWithSalt((String) authentication.getPrincipal());

        if(!passwordEncoder.matches("%s%s".formatted(user.getSalt(), authentication.getCredentials()), user.getPassword())) {
            throw new AuthException("Incorrect credentials","Auth error");
        }
    }
}