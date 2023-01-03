package com.toy.webshop.entity.point;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@Entity
@Getter @Setter
@EntityListeners(value = AuditingEntityListener.class)
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int tradePoint;

    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Enumerated(EnumType.STRING)
    private PointStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    protected PointHistory() {}

    //생성 메서드
    public static PointHistory createPointHistory(Point p, int price) {
        PointHistory pointHistory = new PointHistory();
        pointHistory.setUserId(p.getUser().getId());
        pointHistory.setTradePoint(price);
        pointHistory.setStatus(p.getStatus());
        pointHistory.setPointType(PointType.ORDER);
        pointHistory.setExpiredAt(p.getExpiredAt());
        return pointHistory;
    }
}
