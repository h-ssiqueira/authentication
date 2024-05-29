package com.hss.authentication.controller;

import com.hss.authentication.model.UserCreated;
import com.hss.authentication.model.UserCreationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    @Override
    public ResponseEntity<Void> createUser(String securityToken, UserCreationRequest userCreationRequest) {
        return UserApi.super.createUser(securityToken, userCreationRequest);
    }

    @Override
    public ResponseEntity<UserCreated> fetchUsers(String securityToken, String userId) {
        return UserApi.super.fetchUsers(securityToken, userId);
    }
}
