package com.toy.webshop.repository;


import com.toy.webshop.entity.Address;
import com.toy.webshop.entity.Review;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.item.Book;
import com.toy.webshop.entity.item.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void before() {
        User user = getUser();
        userRepository.save(user);

        Book book1 = createBook("SPRINGBOOT1 BOOK", 10000, 100);
        Book book2 = createBook("SPRINGBOOT2 BOOK", 20000, 100);
        Book book3 = createBook("SPRINGBOOT3 BOOK", 30000, 100);
        Book book4 = createBook("SPRINGBOOT4 BOOK", 40000, 50);

        itemRepository.save(book1);
        itemRepository.save(book2);
        itemRepository.save(book3);
        itemRepository.save(book4);

    }

    @Test
    @DisplayName("create")
    public void 리포지토리테스트() {
        Item findItem = itemRepository.findById(1L).get();
        User findUser = userRepository.findById(1L).get();

        Review review1 = Review.createReview(findItem, findUser, "유익해요~", 4.5f);
        Review review2 = Review.createReview(findItem, findUser, "정말 좋습니다.", 5.0f);
        Review savedReview1 = reviewRepository.save(review1);
        Review savedReview2 = reviewRepository.save(review2);

        List<Review> all = reviewRepository.findAll();

        Assertions.assertThat(all).extracting("score").containsExactly(4.5f, 5.0f);
        Assertions.assertThat(all).extracting("content").containsExactly("유익해요~", "정말 좋습니다.");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        return Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }
    private User getUser() {
        User user = User.builder()
                .email("kdjkmit@naver.com")
                .password("1234")
                .name("김두한")
                .address(new Address("시티","거리","집코드"))
                .build();
        return user;
    }
}