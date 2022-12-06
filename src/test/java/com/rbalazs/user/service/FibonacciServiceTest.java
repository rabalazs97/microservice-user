package com.rbalazs.user.service;

import com.rbalazs.user.dto.FibonacciResponseDTO;
import com.rbalazs.user.dto.FibonacciSaveRequestDTO;
import com.rbalazs.user.model.FibonacciModel;
import com.rbalazs.user.model.UserModel;
import com.rbalazs.user.repo.FibonacciRepo;
import com.rbalazs.user.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FibonacciServiceTest {
    @Mock
    private FibonacciRepo fibonacciRepo;
    @Mock
    private UserRepo userRepo;
    private UserService userService;
    private FibonacciService fibonacciService;

    @BeforeEach
    void setUp(){
        userService = new UserService(userRepo);
        fibonacciService = new FibonacciService(fibonacciRepo, userService);
    }

    @Test
    void shouldGetHistoryAndMapToDTO() {
        int serialNumber = 10;
        BigInteger fib = BigInteger.valueOf(55);
        UserModel user = UserModel.builder().name("Test").build();
        List<FibonacciModel> expectedResponse = Arrays.asList(
                FibonacciModel.builder()
                        .serialNumber(serialNumber)
                        .fibonacciNumber(fib.toByteArray())
                        .userId(user)
                        .build());

        given(fibonacciRepo.findAll()).willReturn(expectedResponse);

        List<FibonacciResponseDTO> response = fibonacciService.getHistory();

        verify(fibonacciRepo).findAll();
        assertEquals(expectedResponse.size(), response.size());
        assertEquals(expectedResponse.get(0).getSerialNumber(), response.get(0).getSerialNumber());
        assertEquals(new BigInteger(expectedResponse.get(0).getFibonacciNumber()), response.get(0).getFibonacciNumber());
    }

    @Test
    void shouldGetEmptyListWhenUserDoesNotExist() {
        given(userService.getUserById(1)).willReturn(Optional.empty());
        List<FibonacciResponseDTO> response = fibonacciService.getUserSpecificHistory(1);
        assertTrue(response.isEmpty());
        verify(fibonacciRepo, never()).findAllByUserId(Mockito.any());
    }

    @Test
    void shouldGetUserHistoryAndMapToDTO() {
        UserModel user = new UserModel(1, "Test");
        int serialNumber = 10;
        BigInteger fib = BigInteger.valueOf(55);
        List<FibonacciModel> expectedResponse = Arrays.asList(
                FibonacciModel.builder()
                        .serialNumber(serialNumber)
                        .fibonacciNumber(fib.toByteArray())
                        .userId(user)
                        .build());
        given(userService.getUserById(user.getId())).willReturn(Optional.of(user));
        given(fibonacciRepo.findAllByUserId(user)).willReturn(expectedResponse);
        List<FibonacciResponseDTO> response = fibonacciService.getUserSpecificHistory(user.getId());

        verify(fibonacciRepo).findAllByUserId(user);
        assertEquals(expectedResponse.size(), response.size());
        assertEquals(expectedResponse.get(0).getSerialNumber(), response.get(0).getSerialNumber());
        assertEquals(new BigInteger(expectedResponse.get(0).getFibonacciNumber()), response.get(0).getFibonacciNumber());
    }

    @Test
    void shouldNotSaveIfUserDoesNotExist() {
        UserModel user = new UserModel(1, "Test");
        int serialNumber = 10;
        BigInteger fib = BigInteger.valueOf(55);
        FibonacciSaveRequestDTO fibonacciSaveRequestDTO = FibonacciSaveRequestDTO.builder()
                .serialNumber(serialNumber)
                .fibonacciNumber(fib.toByteArray())
                .userId(user.getId())
                .build();
        given(userService.getUserById(user.getId())).willReturn(Optional.empty());
        fibonacciService.saveFibonacciToDatabase(fibonacciSaveRequestDTO);
        verify(fibonacciRepo, never()).save(Mockito.any());
    }

    @Test
    void shouldSaveIfUserExists() {
        UserModel user = new UserModel(1, "Test");
        int serialNumber = 10;
        BigInteger fib = BigInteger.valueOf(55);
        FibonacciSaveRequestDTO fibonacciSaveRequestDTO = FibonacciSaveRequestDTO.builder()
                .serialNumber(serialNumber)
                .fibonacciNumber(fib.toByteArray())
                .userId(user.getId())
                .build();
        given(userService.getUserById(user.getId())).willReturn(Optional.of(user));
        fibonacciService.saveFibonacciToDatabase(fibonacciSaveRequestDTO);
        FibonacciModel fibonacciModel = FibonacciModel.builder()
                .serialNumber(fibonacciSaveRequestDTO.getSerialNumber())
                .fibonacciNumber(fibonacciSaveRequestDTO.getFibonacciNumber())
                .userId(user)
                .build();
        verify(fibonacciRepo).save(fibonacciModel);
    }
}