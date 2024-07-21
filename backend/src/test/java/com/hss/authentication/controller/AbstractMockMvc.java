package com.hss.authentication.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.activation.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@ActiveProfiles("test")
@TestInstance(PER_CLASS)
public abstract class AbstractMockMvc {

    protected MockMvc mvc;

    @MockBean
    private DataSource dataSource;

    protected JsonMapper jsonMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeAll
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        jsonMapper = JsonMapper.builder().build();
    }
}