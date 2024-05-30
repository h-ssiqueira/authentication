package com.hss.authentication.controller;

import com.hss.authentication.generated.controller.AuthApi;
import com.hss.authentication.generated.model.AuthenticationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {

    @Override
    public ResponseEntity<AuthenticationToken> login(String username, String password) {
        return null;
    }

    @Override
    public ResponseEntity<Void> logout() {
        return null;
    }
}
