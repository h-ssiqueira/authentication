package com.hss.authentication.domain;

import com.hss.authentication.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static com.hss.authentication.mock.UserMock.pageUserMock;
import static com.hss.authentication.mock.UserMock.userCreationRequestMock;
import static com.hss.authentication.mock.UserMock.userMock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void shouldCreateUserSuccessfully() {
        when(repository.save(any())).thenReturn(userMock());

        service.createUser(userCreationRequestMock());

        verify(repository).save(any());
    }

    @Test
    void shouldFetchUsersSuccessfully() {
        when(repository.findBySearchText(any(), any())).thenReturn(pageUserMock());

        var response = service.fetchUsers("", PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(response.getContent(), hasItem(allOf(
                        hasProperty("username", equalTo("username")),
                        hasProperty("email", equalTo("email@email.com"))
                ))),
                () -> verify(repository).findBySearchText(any(), any())
        );
    }

    @Test
    void shouldFetchUserSuccessfully() {
        when(repository.findById(any())).thenReturn(Optional.of(userMock()));

        var response = service.fetchUser(1L);

        assertAll(
                () -> assertThat(response.orElse(null), allOf(
                        hasProperty("username", equalTo("username")),
                        hasProperty("email", equalTo("email@email.com"))
                )),
                () -> verify(repository).findById(any())
        );
    }
}