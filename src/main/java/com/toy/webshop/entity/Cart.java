package com.toy.webshop.entity;

import com.toy.webshop.entity.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "cartItems")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    //==생성 메서드==//
    public static Cart createCart(User user, CartItem... cartItems) {
        Cart cart = new Cart();
        cart.setUser(user);
        for (CartItem cartItem: cartItems) {
            cart.setCartItems(cartItem);
        }
        return cart;
    }

    //==연관 관계메서드==//
    public void setCartItems(CartItem cartItems) {
        this.cartItems.add(cartItems);
        cartItems.setCart(this);
    }
}
