package com.hss.authentication.domain;

import com.hss.authentication.generated.model.UserCreationRequest;
import com.hss.authentication.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    void createUser(UserCreationRequest dto);

    Page<User> fetchUsers(String searchText, Pageable pageable);

    Optional<User> fetchUser(Long userId);
}