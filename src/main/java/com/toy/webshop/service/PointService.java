package com.toy.webshop.service;

import com.toy.webshop.entity.User;
import com.toy.webshop.entity.point.Point;
import com.toy.webshop.entity.point.PointDto;
import com.toy.webshop.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Point accumulate(User user, Point p) {
        List<Point> findUserPoint = pointRepository.findByUserId(user.getId());
        p = p.calcRemainPoint(findUserPoint);
        Point save = pointRepository.save(p);
        return save;
    }

    public List<PointDto> myPoint(Long userId) {
        List<Point> myPoint = pointRepository.findByUserId(userId);
        return myPoint.stream().map(PointDto::new)
                .collect(Collectors.toList());
    }

    public int getMyPoint(Long userId) {
        List<Point> myPointLists = pointRepository.findByUserId(userId);
        return myPointLists.stream()
                .filter(p -> p.getExpiredAt().isAfter(LocalDateTime.now()))
                .mapToInt(Point::getPrice)
                .sum();
    }

}
