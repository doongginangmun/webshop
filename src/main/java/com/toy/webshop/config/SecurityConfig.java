package com.toy.webshop.config;

import com.toy.webshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final CustomDetails customDetails;
    private final UserLoginAuthenticationProvider userLoginAuthenticationProvider;
    private final DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userLoginAuthenticationProvider)
                .userDetailsService(userService);
    }
    @Bean
    public AuthenticationFailureHandler getCustomFailureHandler() {
        return new CustomAuthFailureHandler();
    }
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices() {
        return new PersistentTokenBasedRememberMeServices("webshop", userService, tokenRepository());
    }
    @Bean
    PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        try {
            repository.removeUserTokens("1");
        }catch (Exception ex) {
            repository.setCreateTableOnStartup(true);
        }
        return repository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(request-> {
                    request.antMatchers("/","/users/new", "/duplicate","/img/**", "/login-error").permitAll()
                            .antMatchers("/users").hasAuthority("ROLE_ADMIN")
                            .antMatchers("/admin","/coupon/new").hasAuthority("ROLE_ADMIN")
                            .antMatchers("/items/new", "/items", "/items/{itemId}/edit", "/items/{itemId}/delete").hasAuthority("ROLE_ADMIN")
                            .anyRequest().authenticated()
                            ;
                })
                .formLogin(login ->
                         login.loginPage("/signin")
                                 .loginProcessingUrl("/loginProcess")
                                 .permitAll()
                                 .defaultSuccessUrl("/", false)
                                 .failureHandler(getCustomFailureHandler())
                                 .authenticationDetailsSource(customDetails)
                                 .successHandler(new MyLoginSuccessHandler())
                )

                .logout(logout-> logout.logoutSuccessUrl("/"))
                .exceptionHandling(error ->
                        error.accessDeniedPage("/access-denied"))
                .rememberMe(r-> r
                        .rememberMeServices(rememberMeServices()));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/favicon.ico", "/error");
    }
}
