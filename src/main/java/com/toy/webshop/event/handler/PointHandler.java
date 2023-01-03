package com.toy.webshop.event.handler;

import com.toy.webshop.entity.Order;
import com.toy.webshop.entity.point.Point;
import com.toy.webshop.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PointHandler {

    private final PointService pointService;

    @EventListener
    public void stack(Order order) { //포인트 적립 일때
        int calcPoint = (int) (order.getOrderPrice() / 100.0);
        Point p = Point.createStackPoint(order.getUser(), calcPoint);
        log.info(" 적립 point = {}", calcPoint);
        pointService.accumulate(p.getUser(), p);
    }

    @EventListener
    public void usePoint(Point p) { //포인트 사용일때
        log.info("사용 point = {}", p.getPrice());
        pointService.accumulate(p.getUser(), p);
    }
}
