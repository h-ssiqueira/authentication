package com.hss.authentication.domain;

import com.hss.authentication.generated.model.AuthenticationToken;

public interface AuthService {

    AuthenticationToken login(String username, String password);

    void logout(String token);
}
