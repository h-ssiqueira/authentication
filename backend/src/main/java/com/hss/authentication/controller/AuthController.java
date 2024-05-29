package com.hss.authentication.controller;

import com.hss.authentication.model.AuthenticationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {

    @Override
    public ResponseEntity<AuthenticationToken> login(String username, String password) {
        return AuthApi.super.login(username, password);
    }

    @Override
    public ResponseEntity<Void> logout(String securityToken) {
        return AuthApi.super.logout(securityToken);
    }
}
