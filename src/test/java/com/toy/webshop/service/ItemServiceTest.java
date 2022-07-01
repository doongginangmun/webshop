package com.toy.webshop.service;

import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.entity.ItemImg;
import com.toy.webshop.entity.item.Book;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.exception.NotExistItemException;
import com.toy.webshop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("아이템 페이징처리")
    public void findItems() {
        //given
        Book book1 = createBook("JPA1 BOOK", 10000, 100);
        Book book2 = createBook("JPA2 BOOK", 20000, 100);
        em.persist(book1);
        em.persist(book2);

        //when
        Pageable pageable = PageRequest.of(0, 2);
        Page<ItemDto> items = itemService.findItems(pageable);

        //then
        assertThat(items.getContent().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("아이템 삭제")
    public void delete() throws Exception {
        //given
        Book book1 = createBook("JPA1 BOOK", 10000, 100);
        Book book2 = createBook("JPA2 BOOK", 20000, 100);

        em.persist(book1);
        em.persist(book2);

        ItemImg itemImg = ItemImg.builder()
                .oriImgName("그림")
                .imgName("그림1")
                .repImgUrl("그림")
                .imgUrl("그림").build();
        itemImg.setItem(book1);
        em.persist(itemImg);

        //when
        itemService.delete(1L);

        //then
        assertThatThrownBy(()-> itemService.findOne(1L))
                .isInstanceOf(NotExistItemException.class);

    }
    private Book createBook(String name, int price, int stockQuantity) {
        return Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();

    }
}