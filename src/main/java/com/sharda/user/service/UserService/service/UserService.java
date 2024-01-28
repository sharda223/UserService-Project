package com.sharda.user.service.UserService.service;

import com.sharda.user.service.UserService.Dto.UserDto;
import com.sharda.user.service.UserService.exception.UserHandledException;
import com.sharda.user.service.UserService.model.User;

import java.util.List;

public interface UserService {
    User saveUser(UserDto userDto) throws UserHandledException;
    UserDto getUser(String userId) throws UserHandledException;
    List<User> getAllUser() throws UserHandledException;
    User updateUser(User user) throws UserHandledException;
    String deleteUser(String userId) throws UserHandledException;
    List<User> getUsersByName(String name) throws UserHandledException;
}
