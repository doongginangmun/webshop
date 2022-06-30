package com.toy.webshop.service;

import com.toy.webshop.entity.SpAuthority;
import com.toy.webshop.entity.User;
import com.toy.webshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public Long save(User user) {
        validateDuplicateUser(user);
        User savedUser = userRepository.save(user);
        addAuthority(savedUser.getId(), "ROLE_USER");
        return savedUser.getId();
    }

    //중복 회원 검증
    private void validateDuplicateUser(User user) {
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        if(findUser.isPresent())
            throw new IllegalStateException("이미 존재하는 회원입니다.");
    }

    //회원가입시 - ajax 중복 Email 검증
    public boolean validateDuplicateEmail(String email) {
        Optional<User> findEmail = userRepository.findByEmail(email);
        if(findEmail.isPresent())
            return true;
        return false;
    }
    //회원 전체 조회
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void addAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            SpAuthority newRole = new SpAuthority(user.getId(), authority);
            if(user.getAuthorities() == null) {
                HashSet<SpAuthority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                userRepository.save(user);
            }else if(!user.getAuthorities().contains(newRole)) {
                HashSet<SpAuthority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(newRole);
                userRepository.save(user);
            }
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
