package com.toy.webshop.dto;

import com.toy.webshop.entity.Cart;
import lombok.Data;


import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartDto {

    private Long id;
    private String userName;
    private List<CartItemDto> cartItems;

    public CartDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public CartDto(Cart cart) {
        this.id = cart.getId();
        this.userName = cart.getUser().getName();
        this.cartItems = cart.getCartItems().stream()
                .map(CartItemDto::new)
                .collect(Collectors.toList());
    }
}
