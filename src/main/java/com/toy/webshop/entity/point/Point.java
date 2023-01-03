package com.toy.webshop.entity.point;

import com.toy.webshop.entity.User;
import com.toy.webshop.entity.discount.AdditionalDiscountPolicy;
import com.toy.webshop.entity.discount.DiscountPolicy;
import com.toy.webshop.entity.discount.NonDiscountPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@EntityListeners(value = {PointEntityListener.class, AuditingEntityListener.class})
@AllArgsConstructor
@ToString
public class Point extends AdditionalDiscountPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int price;

    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Enumerated(EnumType.STRING)
    private PointStatus status;

    private int remainPoint;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    protected Point() {}

    public Point(DiscountPolicy next) {
        super(next);
    }

    public static Point createUsePoint(DiscountPolicy next, User user, int price) {
        Point p = new Point(next);
        p.setUser(user);
        p.setPrice(price);
        p.setPointType(PointType.ORDER);
        p.setStatus(PointStatus.사용);
        p.setExpiredAt(LocalDateTime.now().plusDays(30));
        return p;
    }
    public static Point createStackPoint(User user, int price) {
        Point p = new Point(new NonDiscountPolicy());
        p.setUser(user);
        p.setPrice(price);
        p.setPointType(PointType.ORDER);
        p.setStatus(PointStatus.적립);
        p.setExpiredAt(LocalDateTime.now().plusDays(30));
        return p;
    }
    public Point calcRemainPoint(List<Point> points) {
        if(getStatus()==PointStatus.사용) setPrice(-getPrice());
        if(points.isEmpty()) { //포인트가 0일때 남은 포인트는 적립한 포인트랑 같다.
            setRemainPoint(getPrice());
        }else { //포인트가 이미 있을경우 '시간상 제일 마지막에 적립된 남은포인트' + '적립할 포인트' 해준다.
            Point point = points.get(points.size() - 1);
            setRemainPoint(point.getRemainPoint() + getPrice());
        }
        return this;
    }
    //포인트 사용시 ex) -50 금액으로 계산해야 하기때문에 "+" 사용
    @Override
    protected int afterCalculated(int price) {
        return price + this.getPrice();
    }
}
