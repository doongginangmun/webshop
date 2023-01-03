package com.toy.webshop.service;

import com.toy.webshop.entity.Address;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.discount.NonDiscountPolicy;
import com.toy.webshop.entity.point.Point;
import com.toy.webshop.entity.point.PointHistory;
import com.toy.webshop.repository.PointHistoryRepository;
import com.toy.webshop.repository.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PointServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private PointService pointService;

    @Test
    @DisplayName("포인트적립")
    public void testcase() {
        //given
        User user = getUser();
        Point p1 = Point.createStackPoint(user, 10);
        Point p2 = Point.createStackPoint(user, 30);
        Point p3 = Point.createStackPoint(user, 20);
        //when
        Point accumulate = pointService.accumulate(user, p1);
        pointService.accumulate(user, p2);
        Point accumulate1 = pointService.accumulate(user, p3);
        //then
        System.out.println(accumulate);
        System.out.println("===============");
        System.out.println(accumulate1);
    }

    @Test
    @DisplayName("포인트적립후사용")
    public void testcase2() {
        //given
        User user = getUser();
        Point p1 = Point.createStackPoint(user, 100);
        Point p2 = Point.createUsePoint(new NonDiscountPolicy(), user, 50);
        Point p3 = Point.createStackPoint(user, 199);
        Point p4 = Point.createStackPoint(user, 200);
        Point p5 = Point.createUsePoint(new NonDiscountPolicy(), user, 400);

        //when
        pointService.accumulate(user, p1);
        pointService.accumulate(user, p2);
        pointService.accumulate(user, p3);
        pointService.accumulate(user, p4);
        pointService.accumulate(user, p5);

        //then
        List<Point> all1 = pointRepository.findAll();
        all1.forEach(System.out::println);
        System.out.println("============");
        List<PointHistory> all = pointHistoryRepository.findAll();
        all.forEach(System.out::println);
    }

    @Test
    @DisplayName("만료일이지난포인트")
    public void testcase3() {
        //given
        User user = getUser();
        Point p1 = Point.createStackPoint(user, 10);
        Point p2 = Point.createStackPoint(user, 30);
        Point p3 = Point.createStackPoint(user, 20);
        Point p4 = Point.createStackPoint(user, 20);
        p4.setExpiredAt(LocalDateTime.now().minusDays(7)); //만료일이지나서 getMyPoint에서 무시됨
        //when
        pointService.accumulate(user, p1);
        pointService.accumulate(user, p2);
        pointService.accumulate(user, p3);
        pointService.accumulate(user, p4);
        //then
        int myPoint = pointService.getMyPoint(user.getId());
        assertThat(myPoint).isEqualTo(60);
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