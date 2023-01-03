package com.toy.webshop.repository;

import com.toy.webshop.entity.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findByUserId(Long userId);
}
