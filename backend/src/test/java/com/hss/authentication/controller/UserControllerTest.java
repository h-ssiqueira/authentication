package com.hss.authentication.controller;

import com.hss.authentication.domain.UserService;
import com.hss.authentication.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.hss.authentication.mock.UserMock.emptyPageUserMock;
import static com.hss.authentication.mock.UserMock.pageUserMock;
import static com.hss.authentication.mock.UserMock.userCreationRequestMock;
import static com.hss.authentication.mock.UserMock.userMock;
import static java.util.Optional.empty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
@SpringBootTest(classes = UserController.class)
class UserControllerTest extends AbstractMockMvc {

    private static final String CREATE_AND_AUTOCOMPLETE_USER_URI = "/api/v1/users";
    private static final String FETCH_USER_URI = "/api/v1/users/{userId}";

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    void shouldCreateUserWithSuccess() throws Exception {
        mvc.perform(post(CREATE_AND_AUTOCOMPLETE_USER_URI)
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsBytes(userCreationRequestMock())))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldValidateParametersWhileCreatingUser() throws Exception {
        mvc.perform(post(CREATE_AND_AUTOCOMPLETE_USER_URI)
                        .contentType(APPLICATION_JSON)
                        .content("{}")
                        .accept(APPLICATION_PROBLEM_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.type").value("about:blank"),
                        jsonPath("$.title").value("Bad Request"),
                        jsonPath("$.status").value("400"),
                        jsonPath("$.detail").value("Invalid request content."),
                        jsonPath("$.instance").value(CREATE_AND_AUTOCOMPLETE_USER_URI)
                );
    }

    @Test
    void shouldValidateContentTypeWhileCreatingUser() throws Exception {
        mvc.perform(post(CREATE_AND_AUTOCOMPLETE_USER_URI)
                        .contentType(APPLICATION_XML)
                        .content(""))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldValidateAcceptanceWhileCreatingUser() throws Exception {
        mvc.perform(post(CREATE_AND_AUTOCOMPLETE_USER_URI)
                        .contentType(APPLICATION_JSON)
                        .content("{}")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void shouldFetchUserWithSuccess() throws Exception {
        when(userService.fetchUser(any())).thenReturn(Optional.of(userMock()));

        mvc.perform(get(FETCH_USER_URI, 1L)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).fetchUser(any());
        verify(userMapper).userToResponseDTO(any());
    }

    @Test
    void shouldFetchUserWithSuccessWhenNonExistent() throws Exception {
        when(userService.fetchUser(any())).thenReturn(empty());

        mvc.perform(get(FETCH_USER_URI, 1L)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).fetchUser(any());
        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldValidateLetterIdWhenFetchingUser() throws Exception {
        mvc.perform(get(FETCH_USER_URI, "A")
                        .accept(APPLICATION_PROBLEM_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.type").value("about:blank"),
                        jsonPath("$.title").value("Bad Request"),
                        jsonPath("$.status").value("400"),
                        jsonPath("$.detail").value("Failed to convert 'userId' with value: 'A'"),
                        jsonPath("$.instance").value(FETCH_USER_URI.replace("{userId}", "A"))
                );
    }

    @Test
    void shouldValidateAcceptanceWhenFetchingUser() throws Exception {
        mvc.perform(get(FETCH_USER_URI, 8L)
                        .accept(APPLICATION_XML))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void shouldFetchUsersWithSuccessWithPagination() throws Exception {
        when(userService.fetchUsers(any(), any())).thenReturn(pageUserMock());
        when(userMapper.usersToUserResponseDTOList(any())).thenCallRealMethod();

        mvc.perform(get(CREATE_AND_AUTOCOMPLETE_USER_URI)
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .accept(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        header().exists("X-Has-Next"),
                        header().string("X-Has-Next", "false"),
                        header().exists("X-Total-Size"),
                        header().longValue("X-Total-Size", 1L),
                        header().exists("X-Total-Elements"),
                        header().longValue("X-Total-Elements", 1L),
                        header().exists("X-Total-Pages"),
                        header().longValue("X-Total-Pages", 1L)
                );

        verify(userMapper).usersToUserResponseDTOList(any());
        verify(userService).fetchUsers(any(), any());
    }

    @Test
    void shouldFetchUsersWithSuccessWithAllParametersAndNoData() throws Exception {
        when(userService.fetchUsers(any(), any())).thenReturn(emptyPageUserMock());
        when(userMapper.usersToUserResponseDTOList(any())).thenCallRealMethod();

        mvc.perform(get(CREATE_AND_AUTOCOMPLETE_USER_URI)
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .queryParam("q", "user")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userMapper).usersToUserResponseDTOList(any());
        verify(userService).fetchUsers(any(), any());
    }

    @Test
    void shouldFetchUsersWithSuccessWithNoParameters() throws Exception {
        when(userService.fetchUsers(any(), any())).thenReturn(pageUserMock());
        when(userMapper.usersToUserResponseDTOList(any())).thenCallRealMethod();

        mvc.perform(get(CREATE_AND_AUTOCOMPLETE_USER_URI)
                        .accept(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        header().exists("X-Has-Next"),
                        header().string("X-Has-Next", "false"),
                        header().exists("X-Total-Size"),
                        header().longValue("X-Total-Size", 1L),
                        header().exists("X-Total-Elements"),
                        header().longValue("X-Total-Elements", 1L),
                        header().exists("X-Total-Pages"),
                        header().longValue("X-Total-Pages", 1L)
                );

        verify(userMapper).usersToUserResponseDTOList(any());
        verify(userService).fetchUsers(any(), any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"page", "size"})
    void shouldValidateNumericParametersWhenFetchingUsers(String param) throws Exception {
        mvc.perform(get(CREATE_AND_AUTOCOMPLETE_USER_URI)
                        .queryParam(param, "a")
                        .accept(APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.type").value("about:blank"),
                        jsonPath("$.title").value("Bad Request"),
                        jsonPath("$.status").value("400"),
                        jsonPath("$.detail").value("Failed to convert '%s' with value: 'a'".formatted(param)),
                        jsonPath("$.instance").value(CREATE_AND_AUTOCOMPLETE_USER_URI)
                );
    }

    @Test
    void shouldValidateAcceptanceWhenFetchingUsers() throws Exception {
        mvc.perform(get(CREATE_AND_AUTOCOMPLETE_USER_URI)
                        .accept(APPLICATION_XML))
                .andExpect(status().isNotAcceptable());
    }
}