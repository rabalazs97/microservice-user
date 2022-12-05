package com.rbalazs.user.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbalazs.user.model.FibonacciModel;
import com.rbalazs.user.model.UserModel;
import com.rbalazs.user.repo.FibonacciRepo;
import com.rbalazs.user.dto.FibonacciResponseDTO;
import com.rbalazs.user.dto.FibonacciSaveRequestDTO;

@Service
public class FibonacciService {
    private final FibonacciRepo fibonacciRepo;
    private final UserService userService;

    @Autowired
    public FibonacciService(FibonacciRepo fibonacciRepo, UserService userService){
        this.fibonacciRepo = fibonacciRepo;
        this.userService = userService;
    }

    public List<FibonacciResponseDTO> getHistory() {
        List<FibonacciModel> fibList = fibonacciRepo.findAll();
        return fibList.stream().map(fib -> new FibonacciResponseDTO(fib.getSerialNumber(), new BigInteger(fib.getFibonacciNumber()))).collect(Collectors.toList());
    }

    public List<FibonacciResponseDTO> getUserSpecificHistory(int id) {
        Optional<UserModel> user = userService.getUserById(id);
        if(user.isEmpty()) return new ArrayList<>();
        List<FibonacciModel> fibList = fibonacciRepo.findAllByUserId(user.get());
        return fibList.stream().map(fib -> new FibonacciResponseDTO(fib.getSerialNumber(), new BigInteger(fib.getFibonacciNumber()))).collect(Collectors.toList());
    }

    public void saveFibonacciToDatabase(FibonacciSaveRequestDTO fibonacciSaveRequestDTO){
        Optional<UserModel> user = userService.getUserById(fibonacciSaveRequestDTO.getUserId());
        if(user.isEmpty()) return;
        FibonacciModel fibonacciModel = FibonacciModel.builder()
                                            .serialNumber(fibonacciSaveRequestDTO.getSerialNumber())
                                            .fibonacciNumber(fibonacciSaveRequestDTO.getFibonacciNumber())
                                            .userId(user.get())
                                            .build();
        fibonacciRepo.save(fibonacciModel);
    }
}
