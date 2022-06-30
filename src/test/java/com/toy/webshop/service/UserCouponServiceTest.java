package com.toy.webshop.service;

import com.toy.webshop.entity.Address;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.entity.coupon.CouponType;
import com.toy.webshop.entity.coupon.UserCoupon;
import com.toy.webshop.repository.CouponRepository;
import com.toy.webshop.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserCouponServiceTest {

    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("쿠폰함 쿠폰등록")
    public void testcase() {
        User user = getUser();
        Coupon coupon1 = getCoupon("10프로할인쿠폰", 10, 1, CouponType.PERCENT);
        Coupon coupon2 = getCoupon("20프로할인쿠폰", 10, 1, CouponType.PERCENT);
        Coupon savedCoupon1 = couponRepository.save(coupon1);
        Coupon savedCoupon2 = couponRepository.save(coupon2);
        User savedUser = userRepository.save(user);

        userCouponService.save(user, savedCoupon1.getId());
        userCouponService.save(user, savedCoupon2.getId());

        List<UserCoupon> myCoupon = userCouponService.findMyCoupon(savedUser);
        assertThat(myCoupon.size()).isEqualTo(2);
    }

    private User getUser() {
        User user = User.builder()
                .email("kdjkmit@naver.com")
                .password("1234")
                .name("김두한")
                .address(new Address("시티","거리","집코드"))
                .build();
        return user;
    }

    private Coupon getCoupon(String name, int discountPrice, int quantity, CouponType couponType) {
        return Coupon.createCoupon(
                name,
                discountPrice,
                quantity,
                couponType);
    }

}