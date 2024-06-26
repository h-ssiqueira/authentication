package com.hss.authentication.controller;

import com.hss.authentication.domain.UserService;
import com.hss.authentication.generated.controller.UserApi;
import com.hss.authentication.generated.model.UserCreationRequest;
import com.hss.authentication.generated.model.UserResponseDTO;
import com.hss.authentication.generated.model.UserResponseDTOList;
import com.hss.authentication.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {

    private final UserService userService;
    private final UserMapper usermapper;

    @Override
    public ResponseEntity<Void> createUser(UserCreationRequest userCreationRequest) {
        userService.createUser(userCreationRequest);
        return ResponseEntity.status(CREATED).contentType(APPLICATION_JSON).build();
    }

    @Override
    public ResponseEntity<UserResponseDTO> fetchUser(Long userId) {
        var user = userService.fetchUser(userId);
        return user.map(value -> ResponseEntity.ok(usermapper.userToResponseDTO(value))).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Override
    public ResponseEntity<UserResponseDTOList> fetchUsers(String searchText, Integer page, Integer size) {
        var users = userService.fetchUsers(searchText, buildPageable(page, size));
        var response = usermapper.usersToUserResponseDTOList(users.getContent());
        return users.getContent().isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok().headers(buildHeaders(users)).body(response);
    }

    private HttpHeaders buildHeaders(Page<?> pages) {
        var headers = new HttpHeaders();
        headers.add("X-Has-Next", String.valueOf(pages.hasNext()));
        headers.add("X-Total-Size", String.valueOf(pages.getNumberOfElements()));
        headers.add("X-Total-Elements", String.valueOf(pages.getTotalElements()));
        headers.add("X-Total-Pages", String.valueOf(pages.getTotalPages()));
        return headers;
    }

    private Pageable buildPageable(Integer page, Integer size) {
        return PageRequest.of(nonNull(page) ? page : 0, nonNull(size) ? size : 10);
    }
}