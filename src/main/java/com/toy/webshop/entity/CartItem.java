package com.toy.webshop.entity;

import com.toy.webshop.entity.item.Item;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;
    private int price;



    public static CartItem createCartItem(Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setItem(item);
        cartItem.setCount(count);
        cartItem.setPrice(item.getPrice());
        return cartItem;
    }

    public int getTotalPrice() {
        return getPrice() * getCount();
    }
}
