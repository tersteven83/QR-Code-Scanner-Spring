package com.pcop.qrcode_scanner.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

}
