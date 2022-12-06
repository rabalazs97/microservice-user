package com.rbalazs.user.repo;

import com.rbalazs.user.model.FibonacciModel;
import com.rbalazs.user.model.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FibonacciRepoTest {
    @Autowired
    private FibonacciRepo fibonacciRepo;
    @Autowired
    private UserRepo userRepo;

    @AfterEach
    void tearDown() {
        fibonacciRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void findAllByUserId() {
        String userName = "Test";
        UserModel user = UserModel.builder().name(userName).build();
        userRepo.save(user);

        int serialNumber = 10;
        int serialNumber2 = 11;
        BigInteger fib = BigInteger.valueOf(55);
        BigInteger fib2 = BigInteger.valueOf(89);
        FibonacciModel fibonacciModel = FibonacciModel.builder()
                .serialNumber(serialNumber)
                .fibonacciNumber(fib.toByteArray())
                .userId(user)
                .build();
        FibonacciModel fibonacciModel2 = FibonacciModel.builder()
                .serialNumber(serialNumber2)
                .fibonacciNumber(fib2.toByteArray())
                .userId(user)
                .build();

        fibonacciRepo.save(fibonacciModel);
        fibonacciRepo.save(fibonacciModel2);
        List<FibonacciModel> response = fibonacciRepo.findAllByUserId(user);
        assertEquals(2, response.size());
    }
}