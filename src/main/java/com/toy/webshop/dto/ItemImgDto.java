package com.toy.webshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.toy.webshop.entity.ItemImg;
import lombok.Data;

@Data
public class ItemImgDto {

    private String imgUrl;

    public ItemImgDto(ItemImg itemImg) {
        this.imgUrl = itemImg.getImgUrl();
    }

    @QueryProjection
    public ItemImgDto(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
