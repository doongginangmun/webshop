package com.toy.webshop.repository;

import com.toy.webshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


  boolean existsBycartIdAndItemId(Long cartId, Long itemId);
}
