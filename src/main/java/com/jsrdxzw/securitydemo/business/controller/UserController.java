package com.jsrdxzw.securitydemo.business.controller;

import com.jsrdxzw.securitydemo.business.entity.User;
import com.jsrdxzw.securitydemo.business.server.UserService;
import com.jsrdxzw.securitydemo.security.enity.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    private final CurrentUser currentUser;

    public UserController(UserService userService, CurrentUser currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ROLE_DEV','ROLE_PM','ROLE_ADMIN')")
    public ResponseEntity<Page<User>> getAllUser(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        System.out.println("当前访问该接口的用户为：" + currentUser.getCurrentUser().toString());
        Page<User> pageUser = userService.getAllUser(page, pageSize);
        return ResponseEntity.ok().body(pageUser);
    }


    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<User> deleteUserById(@RequestParam("username") String username) {
        userService.deleteUserByUserName(username);
        return ResponseEntity.ok().build();
    }
}
