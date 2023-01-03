package com.toy.webshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderRequestDto {
    private List<ItemDto> itemDtos = new ArrayList<>();
    private Long couponId;
    private int pointPrice;

    public OrderRequestDto() {
    }

    public OrderRequestDto(List<ItemDto> itemDtos, Long couponId) {
        this.itemDtos = itemDtos;
        this.couponId = couponId;
    }

    public OrderRequestDto(List<ItemDto> itemDtos, Long couponId,int pointPrice) {
        this.itemDtos = itemDtos;
        this.couponId = couponId;
        this.pointPrice = pointPrice;
    }
}
