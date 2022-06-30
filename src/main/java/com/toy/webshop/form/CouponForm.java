package com.toy.webshop.form;

import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.entity.coupon.CouponType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class CouponForm {

    @NotEmpty
    private String name;

    private int discountPrice;

    @Range(min = 10, max = 999)
    private int quantity;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    public Coupon toEntity() {
        return Coupon.createCoupon(name, discountPrice,quantity,couponType);
    }
}
