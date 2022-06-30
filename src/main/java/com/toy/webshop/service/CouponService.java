package com.toy.webshop.service;

import com.toy.webshop.dto.CouponDto;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;


    @Transactional
    public Long createCoupon(Coupon coupon) {
        couponRepository.save(coupon);
        return coupon.getId();
    }

    public Page<CouponDto> findCoupons(Pageable pageable) {
        Page<Coupon> page = couponRepository.findAll(pageable);
        return page.map(m -> new CouponDto(m.getId(), m.getName(), m.getCouponType())); //Page<CouponDto>변환
    }
}
