package com.hss.authentication.domain;

import com.hss.authentication.commons.exception.AuthException;
import com.hss.authentication.generated.model.AuthenticationToken;
import com.hss.authentication.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    @SneakyThrows
    public AuthenticationToken login(String username, String password) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new AuthException("User not found","nonexistent data"));
        var argon2 = new Argon2PasswordEncoder(64,256,1,1024,9);

        if(!argon2.matches("%s%s".formatted(user.getSalt(), password), user.getPassword())) {
            throw new AuthException("Incorrect credentials","Auth error");
        }
        return new AuthenticationToken();
    }

    @Override
    public void logout(String token) {

    }
}