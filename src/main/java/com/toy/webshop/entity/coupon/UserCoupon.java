package com.toy.webshop.entity.coupon;

import com.toy.webshop.entity.Order;
import com.toy.webshop.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class UserCoupon {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private int quantity;

    private LocalDateTime expired_at;

    private boolean used;

    public void setOrder(Order order) {
        this.order = order;
        order.setCoupon(this);
    }

    //생성 메서드
    public static UserCoupon createUserCoupon(User user,Coupon coupon, int quantity) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUser(user);
        userCoupon.setCoupon(coupon);
        userCoupon.setQuantity(quantity);
        userCoupon.setExpired_at(LocalDateTime.now().plusDays(30));
        userCoupon.setUsed(false);
        return userCoupon;
    }

    //비즈니스 로직
    public void usedCoupon() {
        this.used = true;
        this.quantity -= 1;
    }
    public void cancel() {
        this.used = false;
        this.quantity += 1;
    }

}
