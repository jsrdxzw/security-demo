package com.jsrdxzw.securitydemo.business.server;

import com.jsrdxzw.securitydemo.business.entity.User;
import com.jsrdxzw.securitydemo.business.enums.UserStatus;
import com.jsrdxzw.securitydemo.business.exception.UserNameAlreadyExistException;
import com.jsrdxzw.securitydemo.business.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void saveUser(Map<String, String> registerUser) {
        Optional<User> optionalUser = userRepository.findByUsername(registerUser.get("username"));
        if (optionalUser.isPresent()) {
            throw new UserNameAlreadyExistException("User name already exist!Please choose another user name.");
        }
        userRepository.save(User.builder()
                .username(registerUser.get("username"))
                .password(bCryptPasswordEncoder.encode(registerUser.get("password")))
                .roles("DEV,PM")
                .status(UserStatus.CAN_USE)
                .build());
    }

    public User findUserByUserName(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + name));
    }

    public void deleteUserByUserName(String name) {
        userRepository.deleteByUsername(name);
    }

    public Page<User> getAllUser(int page, int pageSize) {
        return userRepository.findAll(PageRequest.of(page, pageSize));
    }

}
