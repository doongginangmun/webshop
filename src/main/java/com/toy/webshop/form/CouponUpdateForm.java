package com.toy.webshop.form;

import com.toy.webshop.entity.coupon.CouponType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter @Setter
@Builder
public class CouponUpdateForm {

    private Long id;
    private String name;
    private int discountPrice;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private CouponType couponType;
}
