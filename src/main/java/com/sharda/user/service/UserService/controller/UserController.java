package com.sharda.user.service.UserService.controller;

import com.sharda.user.service.UserService.Dto.UserDto;
import com.sharda.user.service.UserService.exception.UserHandledException;
import com.sharda.user.service.UserService.model.User;
import com.sharda.user.service.UserService.repo.UserRepository;
import com.sharda.user.service.UserService.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody UserDto user) throws UserHandledException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable String userId) throws UserHandledException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() throws UserHandledException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> getAllUsersByName(@PathVariable String name) throws UserHandledException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsersByName(name));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) throws UserHandledException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user));
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) throws UserHandledException {
        return userService.deleteUser(userId);
    }

}
