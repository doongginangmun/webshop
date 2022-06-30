package com.toy.webshop.dto;

import com.toy.webshop.entity.coupon.CouponType;
import lombok.Data;

@Data
public class CouponDto {

    private Long id;
    private String name;
    private CouponType couponType;

    public CouponDto(Long id, String name, CouponType couponType) {
        this.id = id;
        this.name = name;
        this.couponType = couponType;
    }
}
