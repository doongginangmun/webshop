package com.toy.webshop.controlloer;

import com.toy.webshop.dto.CouponDto;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.form.CouponForm;
import com.toy.webshop.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 생성 폼
     */
    @GetMapping("/coupon/new")
    public String createCouponForm(Model model) {
        model.addAttribute("form", new CouponForm());
        return "coupon/createCouponForm";
    }

    /**
     * 쿠폰 생성
     */
    @PostMapping("/coupon/new")
    public String createCoupon(@Valid @ModelAttribute("form") CouponForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "coupon/createCouponForm";
        }
        Coupon coupon = form.toEntity();
        couponService.createCoupon(coupon);
        return "redirect:/";
    }
    /**
     * 쿠폰 목록
     */
    @GetMapping("/coupons")
    public String list(Pageable pageable, Model model) {
        Page<CouponDto> coupons = couponService.findCoupons(pageable);
        model.addAttribute("coupons", coupons);
        return "coupon/issuedCoupon";
    }

}
