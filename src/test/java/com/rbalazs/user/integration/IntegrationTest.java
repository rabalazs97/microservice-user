package com.rbalazs.user.integration;

import com.rbalazs.user.dto.UserRegistrationDTO;
import com.rbalazs.user.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class IntegrationTest {
    @Autowired
    private UserService userService;

    @Container
    private static final PostgreSQLContainer container = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("integration-test-db")
            .withUsername("sa")
            .withPassword("sa");

    @BeforeAll
    public static void setUp(){
        container.withReuse(true);
        container.start();
    }

    @AfterAll
    public static void tearDown(){
        container.stop();
    }

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
    }

    @Test
    public void testCreateUser(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("balazs");
        userService.addUser(userRegistrationDTO);
    }
}
