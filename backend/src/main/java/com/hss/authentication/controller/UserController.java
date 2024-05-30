package com.hss.authentication.controller;

import com.hss.authentication.domain.UserService;
import com.hss.authentication.mapper.UserMapper;
import com.hss.authentication.generated.controller.UserApi;
import com.hss.authentication.generated.model.UserCreationRequest;
import com.hss.authentication.generated.model.UserResponseDTO;
import com.hss.authentication.generated.model.UserResponseDTOList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {

    private final UserService userService;
    private final UserMapper usermapper;

    @Override
    public ResponseEntity<Void> createUser(UserCreationRequest userCreationRequest) {
        userService.createUser(userCreationRequest);
        return ResponseEntity.status(CREATED).build();
    }

    @Override
    public ResponseEntity<UserResponseDTO> fetchUser(Long userId) {
        var user = userService.fetchUser(userId);
        return user.map(value -> ResponseEntity.ok(usermapper.UserToResponseDTO(value))).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Override
    public ResponseEntity<UserResponseDTOList> fetchUsers(Integer page, Integer size) {
        var users = userService.fetchUsers(PageRequest.of(page, size));
        var response = usermapper.UsersToUserResponseDTOList(users.getContent());
        var headers = new HttpHeaders();
        headers.add("page", String.valueOf(page + 1));
        headers.add("pageSize", String.valueOf(size));
        return users.getContent().isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().headers(headers).body(response);
    }
}
