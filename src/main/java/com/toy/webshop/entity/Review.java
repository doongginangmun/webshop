package com.toy.webshop.entity;

import com.toy.webshop.entity.item.Item;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private String content;

    private float score;

    //연관관계 메서드
    public void setUser(User user) {
        this.user = user;
    }
    public void setItem(Item item) {
        this.item = item;
    }

    //생성 메서드
    public static Review createReview(Item item, User user, String content, float score) {
        Review review = new Review();
        review.setItem(item);
        review.setUser(user);
        review.setContent(content);
        review.setScore(score);
        return review;
    }
}
