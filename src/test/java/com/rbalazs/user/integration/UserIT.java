package com.rbalazs.user.integration;

import com.rbalazs.user.dto.FibonacciResponseDTO;
import com.rbalazs.user.dto.FibonacciSaveRequestDTO;
import com.rbalazs.user.dto.UserRegistrationDTO;
import com.rbalazs.user.model.UserModel;
import com.rbalazs.user.service.FibonacciService;
import com.rbalazs.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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
@Transactional
public class UserIT {
    @Autowired
    private UserService userService;

    @Autowired
    private FibonacciService fibonacciService;

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
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
    public void testCreateUserAndGetUserIdByName(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("testname");
        assertEquals(0, userService.getUserIdByName("testname"));
        userService.addUser(userRegistrationDTO);
        assertNotEquals(0, userService.getUserIdByName("testname"));
    }

    @Test
    public void testGetUserByIdIfExists(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("second_testname");
        userService.addUser(userRegistrationDTO);
        Optional<UserModel> user = userService.getUserById(userService.getUserIdByName("second_testname"));
        assertTrue(user.isPresent());
        assertNotEquals(0, user.get().getId());
        assertEquals("second_testname", user.get().getName());
    }

    @Test
    public void testGetUserByIdIfNotExists(){
        Optional<UserModel> user = userService.getUserById(userService.getUserIdByName("third_testname"));
        assertTrue(user.isEmpty());
    }

    @Test
    public void testSaveFibonacciCalculationAndHistory(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("forth_testname");
        userService.addUser(userRegistrationDTO);
        int id = userService.getUserIdByName("forth_testname");
        UserModel user = userService.getUserById(id).get();
        int serialNumber = 10;
        BigInteger fib = BigInteger.valueOf(55);

        List<FibonacciResponseDTO> before_userspecific = fibonacciService.getUserSpecificHistory(id);
        assertEquals(0, before_userspecific.size());

        FibonacciSaveRequestDTO fibonacciSaveRequestDTO = FibonacciSaveRequestDTO.builder()
                .serialNumber(serialNumber)
                .fibonacciNumber(fib.toByteArray())
                .userId(user.getId())
                .build();

        fibonacciService.saveFibonacciToDatabase(fibonacciSaveRequestDTO);
        List<FibonacciResponseDTO> after_userspecific = fibonacciService.getUserSpecificHistory(id);
        assertEquals(1, after_userspecific.size());
    }

    @Test
    public void testGetHistory(){
        List<FibonacciResponseDTO> result = fibonacciService.getHistory();
        assertEquals(0, result.size());
        UserRegistrationDTO firstUserRegistrationDTO = new UserRegistrationDTO("forth_testname");
        userService.addUser(firstUserRegistrationDTO);
        int firstId = userService.getUserIdByName("forth_testname");
        UserModel firstUser = userService.getUserById(firstId).get();
        UserRegistrationDTO secondUserRegistrationDTO = new UserRegistrationDTO("fifth_testname");
        userService.addUser(secondUserRegistrationDTO);
        int secondId = userService.getUserIdByName("fifth_testname");
        UserModel secondUser = userService.getUserById(secondId).get();
        int serialNumber = 10;
        BigInteger fib = BigInteger.valueOf(55);

        FibonacciSaveRequestDTO firstFibonacciSaveRequestDTO = FibonacciSaveRequestDTO.builder()
        .serialNumber(serialNumber)
        .fibonacciNumber(fib.toByteArray())
        .userId(firstUser.getId())
        .build();
        FibonacciSaveRequestDTO secondFibonacciSaveRequestDTO = FibonacciSaveRequestDTO.builder()
        .serialNumber(serialNumber)
        .fibonacciNumber(fib.toByteArray())
        .userId(secondUser.getId())
        .build();

        fibonacciService.saveFibonacciToDatabase(firstFibonacciSaveRequestDTO);
        assertEquals(1, fibonacciService.getHistory().size());

        fibonacciService.saveFibonacciToDatabase(secondFibonacciSaveRequestDTO);
        assertEquals(2, fibonacciService.getHistory().size());
    }
}
