package com.jsrdxzw.securitydemo.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsrdxzw.securitydemo.security.constants.SecurityConstants;
import com.jsrdxzw.securitydemo.security.enity.JwtUser;
import com.jsrdxzw.securitydemo.security.enity.LoginUser;
import com.jsrdxzw.securitydemo.security.utils.JwtTokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private ThreadLocal<Boolean> rememberMe = new ThreadLocal<>();

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginUser loginUser = objectMapper.readValue(request.getInputStream(), LoginUser.class);
            rememberMe.set(loginUser.getRememberMe());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());
            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 如果验证成功，就生成token并返回
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        List<String> roles = jwtUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), roles, rememberMe.get());
        rememberMe.remove();
        response.setHeader(SecurityConstants.TOKEN_HEADER, token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        rememberMe.remove();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
    }
}
