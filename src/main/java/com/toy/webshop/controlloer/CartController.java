package com.toy.webshop.controlloer;

import com.toy.webshop.config.Login;
import com.toy.webshop.dto.CartDto;
import com.toy.webshop.entity.Cart;
import com.toy.webshop.entity.User;
import com.toy.webshop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 등록되어있는 상품 목록중 담기버튼을 눌러 cart에 담기
     * @param itemId orderForm.html 에서 카트에 담을 아이템 id
     * @param user 로그인한 유저
     */
    @PostMapping("/items/{id}/cart")
    public String putInCart(@PathVariable("id")Long itemId,
                            String count,
                            @Login User user,
                            RedirectAttributes redirectAttributes) {
        log.info("count = {}", count);
        boolean existItem = cartService.existItem(user.getId(), itemId);
        if(existItem) {
            redirectAttributes.addFlashAttribute("id", user.getId());
            redirectAttributes.addFlashAttribute("msg", "이미 장바구니에 있습니다. 이동하시겠습니까?");
            return "redirect:/shopping";
        }
        cartService.putInCart(itemId,Integer.parseInt(count), user);
        return "redirect:/";
    }

    /**cart에 있는 아이템 삭제
     * @param chk  - cartList.html 에서 삭제할 아이템 id
     * @param user - 로그인한 유저
     */
    @DeleteMapping("/items/delete")
    public String deleteItems(Long[] chk, @Login User user) {
        List<Long> longs = Arrays.asList(chk);
        cartService.deleteCartItem(user.getId(), longs);
        return "redirect:/";
    }

    /**
     * 내 장바구니 리스트
     */
    @GetMapping("/items/cart")
    public String myCartList(@Login User user, Model model) {
        CartDto carts = cartService.myCartList(user.getId());
        model.addAttribute("carts", carts);
        return "cart/cartList";
    }
    /**
     * 장바구니 아이템 개수 카운트
     */
    @GetMapping("/items/count")
    @ResponseBody
    public Long cartCount(@Login User user) {
        Long aLong = cartService.countCartItems(user);
        log.info("countCart ={}", aLong);
        return aLong;
    }

}
