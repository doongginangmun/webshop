package com.toy.webshop.repository;

import com.toy.webshop.entity.coupon.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Override
    Page<Coupon> findAll(Pageable pageable);
}
