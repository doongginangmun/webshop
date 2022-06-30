package com.toy.webshop.service;

import com.toy.webshop.dto.CouponDto;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.entity.coupon.CouponType;
import com.toy.webshop.form.CouponForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 만들기")
    public void createCoupon() {
        Coupon coupon = getCoupon("10프로할인쿠폰", 10, 1, CouponType.PERCENT);
        Long findCouponId = couponService.createCoupon(coupon);

        assertThat(findCouponId).isEqualTo(1L);
    }

    @Test
    @DisplayName("등록한전체쿠폰리스트")
    public void findCoupons() {
        Coupon coupon1 = getCoupon("10프로할인쿠폰", 10, 1, CouponType.PERCENT);
        Coupon coupon2 = getCoupon("10000원할인쿠폰", 10000, 1, CouponType.FIXED);
        Coupon coupon3 = getCoupon("2000원할인쿠폰", 2000, 1, CouponType.FIXED);

        couponService.createCoupon(coupon1);
        couponService.createCoupon(coupon2);
        couponService.createCoupon(coupon3);

        PageRequest request = PageRequest.of(0, 2);
        Page<CouponDto> coupons = couponService.findCoupons(request);
        assertThat(coupons.getTotalPages()).isEqualTo(2);
        assertThat(coupons.getSize()).isEqualTo(2);
        assertThat(coupons).extracting("name").containsExactly("10프로할인쿠폰","10000원할인쿠폰");
    }

    private Coupon getCoupon(String name,int discountPrice, int quantity, CouponType couponType) {
        return Coupon.createCoupon(
                name,
                discountPrice,
                quantity,
                couponType);
    }

}