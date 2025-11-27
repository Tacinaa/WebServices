package org.example.tpgrpc.users.service;

import org.example.tpgrpc.users.domain.User;

import java.util.List;

public interface UserService {
    User createUser(String name, String email);
    User getUserById(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);
}