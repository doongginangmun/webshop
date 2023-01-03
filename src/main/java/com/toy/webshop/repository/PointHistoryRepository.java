package com.toy.webshop.repository;

import com.toy.webshop.entity.point.Point;
import com.toy.webshop.entity.point.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findByUserId(Long id);
}
