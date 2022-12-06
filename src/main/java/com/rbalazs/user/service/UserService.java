package com.rbalazs.user.service;

import java.util.Optional;

import com.rbalazs.user.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbalazs.user.dto.UserRegistrationDTO;
import com.rbalazs.user.model.UserModel;
import com.rbalazs.user.repo.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void addUser(UserRegistrationDTO userRegistrationDTO){
        UserModel user = UserModel.builder().name(userRegistrationDTO.getName()).build();
        userRepo.save(user);
    }

    public Integer getUserIdByName(String name) {
        Optional<UserModel> findUserByName = userRepo.findUserByName(name);
        if(findUserByName.isEmpty()) return 0;
        return findUserByName.get().getId();
    }

    public Optional<UserModel> getUserById(int id){
        return userRepo.findById(id);
    }
}
