package com.toy.webshop.config;

import com.toy.webshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserLoginAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /* 사용자가 입력한 정보 */
        String username = authentication.getName();
        String userPw = (String) authentication.getCredentials();
        encoder = new BCryptPasswordEncoder();

        /* DB에서 가져온 정보 (커스터마이징 가능) */
        UserDetails userDetails = userService.loadUserByUsername(username);

        if(encoder.matches(userPw,userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    userDetails,null, userDetails.getAuthorities());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }
}
