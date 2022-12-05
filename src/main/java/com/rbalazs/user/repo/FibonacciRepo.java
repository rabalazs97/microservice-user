package com.rbalazs.user.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rbalazs.user.model.FibonacciModel;
import com.rbalazs.user.model.UserModel;

@Repository
public interface FibonacciRepo extends JpaRepository<FibonacciModel,Integer>{
    List<FibonacciModel> findAllByUserId(UserModel id);
}
