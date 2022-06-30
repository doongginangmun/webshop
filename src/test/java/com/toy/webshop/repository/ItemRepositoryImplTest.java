package com.toy.webshop.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.entity.ItemImg;
import com.toy.webshop.entity.item.Book;
import com.toy.webshop.entity.item.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ItemRepositoryImplTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ItemRepositoryImpl itemRepository;

    private QueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        Book book1 = createBook("SPRINGBOOT1 BOOK", 40000, 100);
        Book book2 = createBook("SPRINGBOOT2 BOOK", 30000, 100);
        Book book3 = createBook("SPRINGBOOT3 BOOK", 20000, 100);
        Book book4 = createBook("SPRINGBOOT4 BOOK", 10000, 50);

        ItemImg itemImg = ItemImg.builder().imgUrl("/images/item/2a739303-dcf6-4412-9c27-afb6bb5b785b.png")
                .repImgUrl("Y")
                .imgName("a")
                .oriImgName("a")
                .build();
        book1.setItemImgs(List.of(itemImg));

        em.persist(book1);
        em.persist(book2);
        em.persist(book3);
        em.persist(book4);

    }

    @Test
    @DisplayName("dsl 페이징")
    public void testcase() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Item> items = itemRepository.items(pageable);

        assertThat(items.getContent().size()).isEqualTo(1);

    }
    @Test
    @DisplayName("dsl 정렬")
    public void sortBy() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.fromString("asc"), "price"));
        Page<Item> items = itemRepository.items(pageable);

        assertThat(items.getContent().size()).isEqualTo(3);

    }
    @Test
    @DisplayName("동적 검색 페이징")
    public void dynamicSearchItems() {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        ItemSearchCondition searchCondition = new ItemSearchCondition();
        searchCondition.setName("SPRINGBOOT");
        Page<ItemDto> itemDtos = itemRepository.dynamicSearchItems(searchCondition, pageable);

        assertThat(itemDtos.getTotalElements()).isEqualTo(4);
        assertThat(itemDtos.getTotalPages()).isEqualTo(2);
    }

    private Book createBook(String name, int price, int stockQuantity) {
        return Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }
}