package com.toy.webshop.event.handler;

import com.toy.webshop.event.CartEvent;
import com.toy.webshop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class CartHandler {

    private final CartService cartService;

    @EventListener
    public void deleteCart(CartEvent dto) {
        log.info("cart Event Handler - deleteCart :" + LocalDateTime.now());
        cartService.deleteCartItem(dto.getFindUser().getId(), dto.getItemIds());
    }


}
