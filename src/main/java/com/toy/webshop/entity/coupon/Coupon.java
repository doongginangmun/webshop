package com.toy.webshop.entity.coupon;

import com.toy.webshop.entity.discount.DiscountPolicy;
import com.toy.webshop.entity.Order;
import com.toy.webshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.stream.IntStream;

@Entity
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Coupon implements DiscountPolicy {

    @Id
    @Column(name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int discountPrice;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    public static Coupon createCoupon(String name, int discountPrice, int quantity, CouponType couponType) {
        Coupon coupon = new Coupon();
        coupon.setName(name);
        coupon.setDiscountPrice(discountPrice);
        coupon.setQuantity(quantity);
        coupon.setCouponType(couponType);
        return coupon;
    }

    @Override
    public int calculatePrice(Order order) {
        IntStream intStream = order.getOrderItems().stream().mapToInt(i ->i.getCount() * i.getOrderPrice());
        int totalPrice = intStream.sum();

        if(couponType==CouponType.PERCENT) {
            return totalPrice - (int)(totalPrice /100.0 * getDiscountPrice());
        }else {
            return Math.max(totalPrice - getDiscountPrice(), 0);
        }
    }

    public void issuedCoupon() {
        quantity = getQuantity()-1;
    }
}
