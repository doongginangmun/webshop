package com.toy.webshop.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter @Setter
@ToString
public class ItemDetailsDto {

    private ItemDto itemDto;
    private Page<ReviewDto> reviews;

    public ItemDetailsDto(ItemDto itemDto, Page<ReviewDto> review) {
        this.itemDto =  itemDto;
        this.reviews = review;
    }
}
