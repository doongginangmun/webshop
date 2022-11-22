package com.toy.webshop.dto;

import com.toy.webshop.entity.Review;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.item.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class ReviewDto {

    private Long id;
    private Item item;
    private User user;
    private String content;
    private String score;
    private LocalDateTime createAt;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.item = review.getItem();
        this.user = review.getUser();
        this.content = review.getContent();
        this.score = calcRating(review.getScore());
        this.createAt = review.getCreatedAt();
    }

    public ReviewDto(Long id, Item item, User user, String content, String score, LocalDateTime createAt) {
        this.id = id;
        this.item = item;
        this.user = user;
        this.content = content;
        this.score = score;
        this.createAt = createAt;
    }

    public String calcRating(float score) {
        StringBuilder star = new StringBuilder();
        for(int i = 0; i < score; i++)
            star.append("⭐");
        return star.toString();
    }
}
