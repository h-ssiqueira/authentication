package com.hss.authentication.mock;

import com.hss.authentication.generated.model.UserCreationRequest;
import com.hss.authentication.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;

import static java.util.Collections.singletonList;

public final class UserMock {

    private UserMock() {
    }

    public static UserCreationRequest userCreationRequestMock() {
        return new UserCreationRequest().email("email@email.com").password("StrongPass:N0ss@").username("username");
    }

    public static User userMock() {
        return User.builder().id(1L).username("username").email("email@email.com").build();
    }

    public static Page<User> pageUserMock() {
        return new PageImpl<>(singletonList(userMock()));
    }

    public static Page<User> emptyPageUserMock() {
        return new PageImpl<>(new ArrayList<>());
    }
}