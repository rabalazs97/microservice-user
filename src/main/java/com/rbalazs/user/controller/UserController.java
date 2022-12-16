package com.rbalazs.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rbalazs.user.dto.FibonacciResponseDTO;
import com.rbalazs.user.dto.FibonacciSaveRequestDTO;
import com.rbalazs.user.dto.UserRegistrationDTO;
import com.rbalazs.user.service.FibonacciService;
import com.rbalazs.user.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController{
    private final UserService userService;
    private final FibonacciService fibonacciService;

    @Autowired
    public UserController(UserService userService, FibonacciService fibonacciService) {
        this.userService = userService;
        this.fibonacciService = fibonacciService;
    }

    @GetMapping
    public String getText(){
        return "Hello buddy!";
    }

    @PostMapping
    public String addUser(@RequestBody UserRegistrationDTO userRegistrationDTO){
        if(userRegistrationDTO.getName() == "") return "Please provide a name!";
        if(userService.getUserIdByName(userRegistrationDTO.getName()) != 0) return "User already exists."; 
        userService.addUser(userRegistrationDTO);
        return "User named " + userRegistrationDTO.getName() + " has been added to the database.";
    }

    @GetMapping("/user/{name}")
    public Integer getUserIdByName(@PathVariable("name") String name){
        return userService.getUserIdByName(name);
    }

    @GetMapping("/history")
    public List<FibonacciResponseDTO> getCalculationHistory(){
        return fibonacciService.getHistory();
    }

    @GetMapping("/history/{id}")
    public List<FibonacciResponseDTO> getUserSpecificCalculationHistory(@PathVariable("id") int id){
        return fibonacciService.getUserSpecificHistory(id);
    }

    @PostMapping("/save")
    public void saveFibonacciToDatabase(@RequestBody FibonacciSaveRequestDTO fibonacciSaveRequestDTO){
        fibonacciService.saveFibonacciToDatabase(fibonacciSaveRequestDTO);
    }
}