package com.hss.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.yml")
@EnableAutoConfiguration(exclude = {LiquibaseAutoConfiguration.class})
class AuthenticationApplicationTest {

    @Autowired
	private ConfigurableApplicationContext applicationContext;

    @Test
    void initialization() {
        assertDoesNotThrow(() -> new AuthenticationApplication());
    }

    @Test
    void context(){
        assertNotNull(applicationContext);
    }
}