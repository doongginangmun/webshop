package com.toy.webshop.form;


import com.toy.webshop.dto.ReviewDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ReviewForm {

    private Long itemId;
    private Long userId;
    private String content;
    private float score;

    public ReviewForm() {
    }

    public ReviewForm(Long itemId, Long userId, String content, float score) {
        this.itemId = itemId;
        this.userId = userId;
        this.content = content;
        this.score = score;
    }

    public ReviewForm(String content, float score) {
        this.content = content;
        this.score = score;
    }
    public ReviewForm(Long itemId, float score, String content) {
        this.itemId = itemId;
        this.content = content;
        this.score = score;
    }
}
