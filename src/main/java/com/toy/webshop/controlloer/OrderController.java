package com.toy.webshop.controlloer;

import com.toy.webshop.config.Login;
import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.dto.OrderDto;
import com.toy.webshop.dto.OrderRequestDto;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.coupon.UserCoupon;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.repository.ItemSearchCondition;
import com.toy.webshop.service.ItemService;
import com.toy.webshop.service.OrderService;
import com.toy.webshop.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final ItemService itemService;
    private final OrderService orderService;
    private final UserCouponService userCouponService;

    /**
     * 판매중인 상품을 볼수있는 폼
     */
    @GetMapping("/shopping")
    public String shoppingForm(@ModelAttribute("itemSearch") ItemSearchCondition condition,
                               @PageableDefault(size = 3) Pageable pageable,
                               Model model) {
        log.info("pageable.getSort()={}", pageable.getSort());
        Page<ItemDto> items = itemService.findItems(pageable);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    /**
     * cartList 폼에서  orderPage 폼으로 주문할 아이템 전송
     * @param user 로그인한 유저
     * @param chk  주문할 아이템 id
     * @param redirectAttributes 선택한 아이템이 하나도 없을경우 cartList 폼으로 redirect
     */
    @PostMapping("/orders")
    public String createForm(Long[] chk,
                             @Login User user, Model model,
                             RedirectAttributes redirectAttributes) {
       if(chk==null) {
           redirectAttributes.addFlashAttribute("msg", "주문할 상품은 한가지 이상 선택 해주세요.");
           return "redirect:/items/cart";
       }
        List<Long> purchaseItemIds = Arrays.asList(chk);
        List<Item> items = itemService.findPurchaseItems(purchaseItemIds);

        List<UserCoupon> coupons = userCouponService.findMyCoupon(user);
//        List<Coupon> coupons = couponService.findCouponList(user.getId());

        List<ItemDto> itemList = items.stream()
                .map(ItemDto::new)
                .collect(Collectors.toList());
        model.addAttribute("itemList", itemList);
        model.addAttribute("coupons",coupons);
        model.addAttribute("OrderRequestForm", new OrderRequestDto());
        return "order/orderPage";
    }

    /**
     * orderPage에서 상품 주문
     * @Param chk  주문할 아이템 id값
     * @Param user 로그인한 유저
     */
    @PostMapping(value = "/order")
    public String order(OrderRequestDto requestDto,
                        @Login User user,
                        RedirectAttributes redirectAttributes) {
        if(requestDto.getChk().isEmpty() || requestDto.getChk().size()==0) {
            redirectAttributes.addFlashAttribute("msg", "주문할 상품은 한가지 이상 선택 해주세요.");
            return "redirect:/items/cart";
        }
        if(requestDto.getCouponId()==0)
            orderService.order(user, 1, requestDto.getChk());
        else
            orderService.orderCoupon(user, 1, requestDto.getCouponId(), requestDto.getChk());
        return "redirect:/";
    }

    /**
     * 내가 주문한 리스트 반환
     */
    @GetMapping("/orderList")
    public String orderList(@Login User user, Model model) {
        List<OrderDto> orderDtos = orderService.orderList(user.getId());
        model.addAttribute("orders", orderDtos);
        return "order/orderList";
    }

    @DeleteMapping("/orders/{orderId}/delete")
    public String orderCancel(@PathVariable("orderId") Long id) {
        log.info("id={}", id);
        orderService.cancelOrder(id);
        return "redirect:/orderList";
    }
}
