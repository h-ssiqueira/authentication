package com.hss.authentication.domain;

import com.hss.authentication.generated.model.AuthenticationToken;
import com.hss.authentication.persistence.model.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @SneakyThrows
    public AuthenticationToken login(UsernamePasswordAuthenticationToken credentials) {
        var auth = authenticationManager.authenticate(credentials);
        var token = tokenService.generateToken((String) auth.getPrincipal());
        return new AuthenticationToken().token(token);
    }

    @Override
    public void logout(String token) {

    }
}