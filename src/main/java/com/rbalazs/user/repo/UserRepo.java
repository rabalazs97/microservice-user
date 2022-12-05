package com.rbalazs.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rbalazs.user.model.UserModel;

@Repository
public interface UserRepo extends JpaRepository<UserModel, Integer>{
    @Query("SELECT u FROM UserModel u WHERE u.name = ?1")
    Optional<UserModel> findUserByName(String name);
}
