package com.hss.authentication.domain;

import com.hss.authentication.generated.model.AuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface AuthService {

    AuthenticationToken login(UsernamePasswordAuthenticationToken credentials);

    void logout(String token);
}