package com.hss.authentication.domain;

import com.hss.authentication.generated.model.UserCreationRequest;
import com.hss.authentication.persistence.UserRepository;
import com.hss.authentication.persistence.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(UserCreationRequest dto) {
        var newUser = User.builder().username(dto.getUsername()).email(dto.getEmail()).build();
        var salt = new Base64StringKeyGenerator(73).generateKey();
        var password = new Argon2PasswordEncoder(64,256,1,1024,9).encode("%s%s".formatted(salt,dto.getPassword()));

        newUser.setPassword(password);
        newUser.setSalt(salt);
        var id = userRepository.save(newUser);
        log.info("Created user with id: {}", id);
    }

    @Override
    public Page<User> fetchUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> fetchUser(Long userId) {
        return userRepository.findById(userId);
    }
}
