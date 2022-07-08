package com.toy.webshop.service;

import com.toy.webshop.dto.CouponDto;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.exception.NotExistItemException;
import com.toy.webshop.form.CouponForm;
import com.toy.webshop.form.CouponUpdateForm;
import com.toy.webshop.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public Coupon findOne(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(NotExistItemException::new);
    }
    @Transactional
    public Long updateCoupon(CouponUpdateForm form) {
        Coupon findCoupon = couponRepository.findById(form.getId()).orElseThrow(NotExistItemException::new);
        findCoupon.setName(form.getName());
        findCoupon.setQuantity(form.getQuantity());
        findCoupon.setDiscountPrice(form.getDiscountPrice());
        findCoupon.setCouponType(form.getCouponType());

        return findCoupon.getId();
    }

    public void deleteCoupon(Long couponId) {
        Coupon findCoupon = findOne(couponId);
        couponRepository.delete(findCoupon);
    }
}
