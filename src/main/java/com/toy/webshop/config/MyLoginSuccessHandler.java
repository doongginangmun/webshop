package com.toy.webshop.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {
    String url = "";
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("MyLoginSuccessHandler 실행"+ authentication.getAuthorities().toString());
        if(authentication.getAuthorities().toString().contains("ROLE_ADMIN")) {
            url = "/admin";
        }else if(authentication.getAuthorities().toString().contains("ROLE_USER")) {
            url = "/";
        }
        response.sendRedirect(url);
    }
}
