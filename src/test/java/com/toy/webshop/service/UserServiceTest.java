package com.toy.webshop.service;

import com.toy.webshop.entity.Role;
import com.toy.webshop.entity.User;
import com.toy.webshop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    @InjectMocks
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
        doReturn(1L).when(userService).save(any(User.class));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Long save = userService.save(user);
        Optional<User> user1 = userRepository.findById(save);

        //then
        assertThat(user1.get().getEmail()).isEqualTo(user.getEmail());
        verify(userService, times(1)).save(any());

    }
    @Test
    @DisplayName("이메일 중복 여부.")
    public void testcase2() {
        //given
        User user1 = User.builder()
                .email("test@test.com")
                .password("1234")
                .name("김두한")
                .build();
        //when
        doReturn(true).when(userService).validateDuplicateEmail(user1.getEmail());
        boolean duplicateEmail = userService.validateDuplicateEmail(user1.getEmail());

        verify(userService, times(1)).validateDuplicateEmail(any());
        assertThat(duplicateEmail).isTrue();
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

        List<User> userList1 = Arrays.asList(user1, user2, user3);
        doReturn(userList1).when(userService).findAll();
        List<User> userList = userService.findAll();
        //then
        assertThat(userList.size()).isEqualTo(3);
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
        doReturn(Optional.of(user)).when(userRepository).findByEmail(user.getEmail());
        User byEmail = userRepository.findByEmail(user.getEmail()).get();

        //then
        System.out.println(byEmail.getCreatedAt());
        assertThat(byEmail.getCreatedAt()).isEqualTo(user.getCreatedAt());

    }
}