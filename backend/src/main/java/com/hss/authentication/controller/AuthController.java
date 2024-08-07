package com.hss.authentication.controller;

import com.hss.authentication.domain.AuthService;
import com.hss.authentication.generated.controller.AuthApi;
import com.hss.authentication.generated.model.AuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthenticationToken> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        var response = authService.login(new UsernamePasswordAuthenticationToken(username,password));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> logout() {
        return null;
    }
}