package com.hss.authentication.domain;

import com.hss.authentication.commons.exception.AuthException;
import com.hss.authentication.persistence.model.User;

public interface TokenService {
    String generateToken(String username) throws AuthException;

    String validateToken(String token);
}