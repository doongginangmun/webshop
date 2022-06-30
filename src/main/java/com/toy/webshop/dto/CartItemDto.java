package com.toy.webshop.dto;

import com.toy.webshop.entity.CartItem;
import lombok.Data;

@Data
public class CartItemDto {

    private Long id;
    private String itemName;
    private int price;
    private int count;

    public CartItemDto(Long id, String itemName, int price, int count) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
    }

    public CartItemDto(CartItem cartItem) {
        this.id = cartItem.getItem().getId();
        this.itemName = cartItem.getItem().getName();
        this.price = cartItem.getPrice();
        this.count = cartItem.getCount();
    }
}
