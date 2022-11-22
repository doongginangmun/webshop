package com.toy.webshop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.toy.webshop.entity.Review;

import com.toy.webshop.repository.support.QuerydslItemRepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.toy.webshop.entity.QReview.review;
import static com.toy.webshop.entity.item.QItem.item;
import static org.springframework.util.StringUtils.hasText;

public class ReviewRepositoryImpl extends QuerydslItemRepositorySupport implements ReviewRepositoryCustom {

    public ReviewRepositoryImpl() { super(Review.class); }

    @Override
    public Page<Review> reviews(String itemName, Pageable pageable) {
        JPAQuery<Long> countQuery = reviewCount();
        return applyPagination(pageable, countQuery, contentQuery -> contentQuery.selectFrom(review)
                .where(itemNameContains(itemName)));
    }
    //리뷰 전체 조회
    @Override
    public JPAQuery<Long> reviewCount() {
        return select(Wildcard.count).from(review);
    }

    private BooleanExpression itemNameContains(String name) {
        return hasText(name) ? item.name.containsIgnoreCase(name) : null;
    }
}
