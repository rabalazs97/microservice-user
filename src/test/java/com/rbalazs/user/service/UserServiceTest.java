package com.rbalazs.user.service;

import com.rbalazs.user.dto.UserRegistrationDTO;
import com.rbalazs.user.model.UserModel;
import com.rbalazs.user.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepo);
    }

    @Test
    void ableToAddUser() {
        UserRegistrationDTO user = new UserRegistrationDTO("Test");
        userService.addUser(user);

        ArgumentCaptor<UserModel> userModelArgumentCaptor =
                ArgumentCaptor.forClass(UserModel.class);

        verify(userRepo).save(userModelArgumentCaptor.capture());

        UserModel capturedUser = userModelArgumentCaptor.getValue();

        assertEquals(user.getName(), capturedUser.getName());
    }

    @Test
    void shouldReturn0ForIdIfNameDoesNotExist() {
        String test = "Test";
        given(userRepo.findUserByName(test)).willReturn(Optional.empty());
        Integer response = userService.getUserIdByName(test);
        assertEquals(0, response);
    }

    @Test
    void shouldReturnUserIdByNameIfExists() {
        UserModel user = new UserModel(1, "Test");
        given(userRepo.findUserByName(user.getName())).willReturn(Optional.of(user));
        Integer response = userService.getUserIdByName(user.getName());
        assertEquals(user.getId(), response);
    }

    @Test
    void ableToFindUserById() {
        userService.getUserById(Mockito.anyInt());
        verify(userRepo).findById(Mockito.anyInt());
    }
}