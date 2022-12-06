package com.rbalazs.user.repo;

import com.rbalazs.user.model.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepoTest {
    @Autowired
    private UserRepo userRepo;

    @AfterEach
    void tearDown(){
        userRepo.deleteAll();
    }

    @Test
    void itShouldFindUserByName() {
        String userName = "Test";
        UserModel user = UserModel.builder().name(userName).build();
        userRepo.save(user);
        Optional<UserModel> response = userRepo.findUserByName(userName);
        assertEquals(user.getId(), response.get().getId());
        assertEquals(user.getName(), response.get().getName());
    }

    @Test
    void itShouldReturnEmptyIfNameDoesNotExist() {
        Optional<UserModel> response = userRepo.findUserByName("Test");
        assertTrue(response.isEmpty());
    }
}