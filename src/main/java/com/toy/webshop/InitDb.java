package com.toy.webshop;

import com.toy.webshop.entity.*;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.entity.coupon.CouponType;
import com.toy.webshop.entity.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;
    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {
            User user = createUser("user@naver.com",
                    "$2a$10$J2QNg1TfPlBw1LMWCeuK6udmsW7sw77R6onfDbQTxqngyv5VZYFbG","김두한", "서울", "1", "1111",true);
            em.persist(user);
            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);
            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);
            SpAuthority spAuthority = createAuthority(1L, "ROLE_USER");
            em.persist(spAuthority);
        }
        public void dbInit2() {
            User user = createUser("admin", "$2a$10$J2QNg1TfPlBw1LMWCeuK6udmsW7sw77R6onfDbQTxqngyv5VZYFbG","admin", "진주", "2", "2222", true);
            SpAuthority spAuthority = createAuthority(2L, "ROLE_ADMIN");
            em.persist(user);
            em.persist(spAuthority);
        }

        private SpAuthority createAuthority(Long userId, String authority) {
            SpAuthority spAuthority = new SpAuthority();
            spAuthority.setUserId(userId);
            spAuthority.setAuthority(authority);
            return spAuthority;
        }

        private User createUser(String email, String password,String name,
                                    String city, String street,
                                    String zipcode, boolean enabled) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setName(name);
            user.setEnabled(true);
            user.setAddress(new Address(city, street, zipcode));
            return user;
        }
        private Book createBook(String name, int price, int stockQuantity) {
            return Book.builder()
                    .name(name)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .build();

        }
        private Delivery createDelivery(User user) {
            Delivery delivery = new Delivery();
            delivery.setAddress(user.getAddress());
            return delivery;
        }
    }
}