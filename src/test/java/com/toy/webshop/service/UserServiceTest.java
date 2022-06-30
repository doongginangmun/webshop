package com.toy.webshop.service;

import com.toy.webshop.entity.Role;
import com.toy.webshop.entity.User;
import com.toy.webshop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원가입")
    public void testcase1() {
        //given
        User user = User.builder()
                .email("test@test.com")
                .password("1234")
                .name("김두한")
                .build();
        //when
        Long saveId = userService.save(user);
        //then
        assertThat(saveId).isEqualTo(user.getId());
    }
    @Test
    @DisplayName("중복 회원 가입시 에러발생 해야한다.")
    public void testcase2() {
        //given
        User user1 = User.builder()
                .email("test@test.com")
                .password("1234")
                .name("김두한")
                .build();

        User user2 = User.builder()
                .email("test@test.com")
                .password("1234")
                .name("김두한")
                .build();
        //when
        userService.save(user1);

        assertThatThrownBy(() -> {
            userService.save(user2);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("전체 회원 조회")
    public void testcase3() {
        //given
        User user1 = User.builder()
                .email("test@test.com")
                .password("1234")
                .name("김두한")
                .build();

        User user2 = User.builder()
                .email("tester1@test.com")
                .password("1234")
                .name("김머랭")
                .build();

        User user3 = User.builder()
                .email("tester2@test.com")
                .password("1234")
                .name("김쿠키")
                .build();

        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        List<User> userList = userService.findAll();

        //then
        assertThat(userList.size()).isEqualTo(5);

    }
    @Test
    @DisplayName("auditing")
    public void testcase4() {
        //given
        User user = User.builder()
                .email("test@test.com")
                .password("1234")
                .name("김두한")
                .build();
        //when
        userService.save(user);
        Optional<User> one = userRepository.findByEmail(user.getEmail());

        //then
        System.out.println(one.get().getCreatedAt());
        assertThat(one.get().getCreatedAt()).isEqualTo(user.getCreatedAt());

    }
}