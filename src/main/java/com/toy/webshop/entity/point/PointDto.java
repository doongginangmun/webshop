package com.toy.webshop.entity.point;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PointDto {

    private int price;
    private PointType type;
    private PointStatus status;
    private int remainPoint;
    private LocalDateTime createAt;
    private LocalDateTime expiredAt;

    public PointDto(Point p) {
        price = p.getPrice();
        type = p.getPointType();
        status = p.getStatus();
        remainPoint = p.getRemainPoint();
        createAt = p.getCreatedAt();
        expiredAt = p.getExpiredAt();
    }
}
