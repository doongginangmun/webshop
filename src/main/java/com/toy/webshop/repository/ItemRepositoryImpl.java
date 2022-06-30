package com.toy.webshop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.dto.ItemImgDto;

import com.toy.webshop.entity.item.Item;
import com.toy.webshop.repository.support.QuerydslItemRepositorySupport;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.toy.webshop.entity.item.QItem.*;
import static org.springframework.util.StringUtils.*;

public class ItemRepositoryImpl extends QuerydslItemRepositorySupport implements ItemRepositoryCustom {

    public ItemRepositoryImpl() {
        super(Item.class);
    }

    @Override
    public Page<ItemDto> dynamicSearchItems(ItemSearchCondition condition, Pageable pageable) {

        List<Item> fetch =
                selectFrom(item)
                .where(itemNameContains(condition.getName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = select(Wildcard.count)
                .from(item)
                .where(itemNameContains(condition.getName()));

        //dto변환 변환할때 @OneToMany 관계인 itemImgs in절 쿼리 한번에 가져온다.
        List<ItemDto> collect = fetch.stream().map(i -> new ItemDto(i.getId(), i.getName(), i.getPrice(), i.getStockQuantity(),
                i.getItemImgs().stream().map(ItemImgDto::new).collect(Collectors.toList()))).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(collect, pageable, countQuery::fetchOne);
    }

    /**
     * querydsl repositorySupport 사용
     * select Item 쿼리 - 1
     * select ItemImg 쿼리 - 1
     * count(*) 쿼리 - 1
     * 총 3번의 쿼리 발생
     */
    @Override
    public Page<Item> items(Pageable pageable) {
        JPAQuery<Long> countQuery = itemCount();
        return applyPagination(pageable, countQuery, contentQuery -> contentQuery.selectFrom(item));
    }

    //아이템 전체 개수 조회
    @Override
    public JPAQuery<Long> itemCount() {
        return select(Wildcard.count).from(item);
    }

    private BooleanExpression itemNameContains(String name) {
        return hasText(name) ? item.name.containsIgnoreCase(name) : null;
    }
}
