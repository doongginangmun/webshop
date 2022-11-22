package com.toy.webshop.controlloer;

import com.toy.webshop.config.Login;
import com.toy.webshop.dto.*;
import com.toy.webshop.entity.User;
import com.toy.webshop.form.ReviewForm;
import com.toy.webshop.service.ItemService;
import com.toy.webshop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ItemService itemService;

    @PostMapping("/review/new")
    public String registryReview(@RequestBody ReviewForm form,
                                 @PageableDefault(size = 3) Pageable pageable,
                                 @Login User user,
                                 Model model) {
        form.setUserId(user.getId());

        reviewService.save(form);
        extracted(pageable, form.getItemId(), model);

        return "items/itemDetail :: .comment";
    }
    @DeleteMapping("/review/delete/{reviewId}")
    public String deleteReview(@PathVariable("reviewId") Long reviewId,
                               @PageableDefault(size = 3) Pageable pageable,
                               Long itemId,
                               Model model) {
        reviewService.delete(reviewId);
        extracted(pageable, itemId, model);

        return "items/itemDetail :: .comment";
    }

    private void extracted(@PageableDefault(size = 3) Pageable pageable, Long itemId, Model model) {
        ItemDto books = itemService.findBooks(itemId);
        //Entity -> Dto변환
        Page<ReviewDto> collect = reviewService.findItemReviewAll(books.getName(), pageable).map(ReviewDto::new);

        ItemDetailsDto itemDetails = new ItemDetailsDto(books, collect);
        model.addAttribute("itemDetails", itemDetails);
    }

}
