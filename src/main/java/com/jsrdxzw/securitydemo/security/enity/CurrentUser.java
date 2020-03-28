package com.jsrdxzw.securitydemo.security.enity;

import com.jsrdxzw.securitydemo.security.server.UserDetailsServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 获取当前请求的用户
 */
@Component
public class CurrentUser {
    private final UserDetailsServiceImpl userDetailsService;

    public CurrentUser(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public JwtUser getCurrentUser() {
        return (JwtUser) userDetailsService.loadUserByUsername(getCurrentUserName());
    }

    private static String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }
}
