package com.toy.webshop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.entity.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface ItemRepositoryCustom {

    Page<ItemDto> dynamicSearchItems(ItemSearchCondition condition, Pageable pageable);
    Page<Item> items(Pageable pageable);
    JPAQuery<Long> itemCount();
    Page<Item> findAll(Pageable pageable);
}
