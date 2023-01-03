package com.toy.webshop.repository;

import com.toy.webshop.entity.Address;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.discount.NonDiscountPolicy;
import com.toy.webshop.entity.point.Point;
import com.toy.webshop.entity.point.PointStatus;
import com.toy.webshop.entity.point.PointType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class PointRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PointRepository pointRepository;

    @Test
    @DisplayName("포인트적립")
    public void testcase() {
        //given
        User user = getUser();
        Point p = Point.createStackPoint(user, 100);
        //when
        Point save = pointRepository.save(p);
        //then
        System.out.println(save);
    }
    @Test
    @DisplayName("포인트사용")
    public void testcase2() {
        //given
        User user = getUser();
        Point p = Point.createUsePoint(new NonDiscountPolicy(), user, -50);
        //when
        Point save = pointRepository.save(p);
        //then
        System.out.println(save);
    }

    private User getUser() {
        User user = User.builder()
                .email("kdjkmit@naver.com")
                .password("1234")
                .name("김두한")
                .address(new Address("시티","거리","집코드"))
                .build();
        em.persist(user);
        return user;
    }
}