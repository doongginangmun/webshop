package com.toy.webshop.event.handler;

import com.toy.webshop.entity.Order;
import com.toy.webshop.event.CouponEvent;
import com.toy.webshop.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class CouponHandler {

    private final UserCouponRepository couponRepository;

    @EventListener
    public void useCoupon(CouponEvent o) {
        log.info("coupon Event Handler - useCoupon :" + LocalDateTime.now());
        o.getCoupon().setOrder(o.getOrder());
        o.getCoupon().usedCoupon();
    }
}
