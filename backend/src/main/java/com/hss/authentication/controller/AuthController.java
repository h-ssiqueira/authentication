package com.hss.authentication.controller;

import com.hss.authentication.domain.AuthService;
import com.hss.authentication.generated.controller.AuthApi;
import com.hss.authentication.generated.model.AuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthenticationToken> login(@RequestPart("username") String username, @RequestPart("password") String password) {
        var response = authService.login(username, password);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> logout() {
        return null;
    }
}