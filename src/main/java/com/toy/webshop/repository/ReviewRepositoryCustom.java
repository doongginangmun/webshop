package com.toy.webshop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.toy.webshop.entity.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    Page<Review> reviews(String itemName, Pageable pageable);
    JPAQuery<Long> reviewCount();
}
