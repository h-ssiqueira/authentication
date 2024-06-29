package com.hss.authentication.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.activation.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
public abstract class AbstractMockMvc {

    protected MockMvc mvc;

    @MockBean
    private DataSource dataSource;

    protected JsonMapper jsonMapper = JsonMapper.builder().build();

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}