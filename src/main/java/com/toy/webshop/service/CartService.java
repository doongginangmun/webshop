package com.toy.webshop.service;

import com.toy.webshop.dto.CartDto;
import com.toy.webshop.dto.CartItemDto;
import com.toy.webshop.entity.*;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.exception.NotExistItemException;
import com.toy.webshop.repository.CartItemRepository;
import com.toy.webshop.repository.CartRepository;
import com.toy.webshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

   private final CartRepository cartRepository;
   private final ItemRepository itemRepository;
   private final CartItemRepository cartItemRepository;

   public boolean existItem(Long userId, Long itemId) {
       Cart findCart = getCart(userId);

       if(findCart==null) return false;

       return cartItemRepository.existsBycartIdAndItemId(findCart.getId(), itemId);
   }

   @Transactional
   public Long putInCart(Long itemId, User user) {
       Cart findCart = getCart(user.getId());

       Item findItem = itemRepository.findById(itemId)
               .orElseThrow(NotExistItemException::new);

       CartItem cartItem = CartItem.createCartItem(findItem);

       if(findCart!=null) {
          return updateCartItem(cartItem, findCart);
       }
        Cart cart = Cart.createCart(user, cartItem);
        cartRepository.save(cart);

       return cart.getId();
    }

    private Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Long updateCartItem(CartItem cartItem, Cart cart) {
        cart.setCartItems(cartItem);
        return cart.getId();
    }

    @Transactional
    public void deleteCartItem(Long userId, Collection<Long> itemId) {
        Cart findCart = getCart(userId);
        cartRepository.delete(findCart.getId(), itemId);
    }

    @Transactional
    public CartDto myCartList(Long userId) {
        CartDto cartDto = cartRepository.findCart(userId);
        List<CartItemDto> cartItems = cartRepository.findCartItems(cartDto.getId());
        cartDto.setCartItems(cartItems);

        return cartDto;
    }
}
