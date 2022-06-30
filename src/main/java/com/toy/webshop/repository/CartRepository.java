package com.toy.webshop.repository;

import com.toy.webshop.dto.CartDto;
import com.toy.webshop.dto.CartItemDto;
import com.toy.webshop.dto.OrderDto;
import com.toy.webshop.dto.OrderItemDto;
import com.toy.webshop.entity.Cart;
import com.toy.webshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);

    @Query("select new com.toy.webshop.dto.CartDto(c.id, u.name)" +
            " from Cart c join c.user u where u.id =:userId")
    CartDto findCart(@Param("userId") Long userId);

    @Query("select new com.toy.webshop.dto.CartItemDto(i.id, i.name, ci.price, ci.count)" +
            " from CartItem ci join ci.item i where ci.cart.id =:cartId")
    List<CartItemDto> findCartItems(@Param("cartId") Long cartId);

    @Query("select ci from CartItem ci where ci.cart.id =:cartId and ci.item.id in :itemIds")
    List<CartItem> findDeleteCartItems(@Param("itemIds") Collection<Long> itemIds, @Param("cartId") Long cartId);

    @Modifying
    @Transactional
    @Query("delete from CartItem ci where ci.cart.id =:cartId and ci.item.id in :itemId")
    void delete(@Param("cartId")Long cartId, @Param("itemId") Collection<Long> itemId);
}
