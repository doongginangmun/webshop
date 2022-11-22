package com.toy.webshop.service;

import com.toy.webshop.entity.Review;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.exception.NotExistItemException;
import com.toy.webshop.exception.NotFoundUserException;
import com.toy.webshop.form.ReviewForm;
import com.toy.webshop.repository.ItemRepository;
import com.toy.webshop.repository.ReviewRepository;
import com.toy.webshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    public Long save(ReviewForm form) {
        Item item = itemRepository.findById(form.getItemId())
                .orElseThrow(NotExistItemException::new);
        User user = userRepository.findById(form.getUserId())
                .orElseThrow(NotFoundUserException::new);

        Review review = Review.createReview(item, user, form.getContent(), form.getScore());
        Review savedReview = reviewRepository.save(review);

        return savedReview.getId();
    }

    public Page<Review> findItemReviewAll(String itemName, Pageable pageable) {
        return reviewRepository.reviews(itemName, pageable);
    }

    public void delete(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
