package com.vicnetto.springsecurity.security.user.service;

import com.vicnetto.springsecurity.security.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Integer userId);

    Optional<User> getUserById(Integer id);

    Optional<User> getUserByUsername(String username);

    List<User> getUsers();
}
