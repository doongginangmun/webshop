package com.toy.webshop.controlloer;

import com.toy.webshop.dto.CouponDto;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.form.CouponForm;
import com.toy.webshop.form.CouponUpdateForm;
import com.toy.webshop.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * 상품 수정 폼 전송
     */
    @GetMapping("/coupon/{couponId}/edit")
    public String updateItemForm(@PathVariable("couponId") Long couponId, Model model) {
        Coupon findCoupon = couponService.findOne(couponId);

        CouponUpdateForm form = CouponUpdateForm.builder()
                .id(findCoupon.getId())
                .name(findCoupon.getName())
                .discountPrice(findCoupon.getDiscountPrice())
                .quantity(findCoupon.getQuantity())
                .couponType(findCoupon.getCouponType())
                .build();

        model.addAttribute("form", form);

        return "coupon/updateCouponForm";
    }
    /**
     * 쿠폰 수정
     */
    @PostMapping("/coupon/{couponId}/edit")
    public String updateCoupon(@ModelAttribute("form") CouponUpdateForm form) {
        couponService.updateCoupon(form);
        return "redirect:/coupon";
    }
    /**
     * 발급 받을 쿠폰 목록
     */
    @GetMapping("/coupons")
    public String list(Pageable pageable, Model model) {
        Page<CouponDto> coupons = couponService.findCoupons(pageable);
        model.addAttribute("coupons", coupons);
        return "coupon/issuedCoupon";
    }

    /**
     * 전체 쿠폰 목록 - 관리
     */
    @GetMapping("/coupon")
    public String couponList(Pageable pageable, Model model) {
        Page<CouponDto> coupons = couponService.findCoupons(pageable);
        model.addAttribute("coupons", coupons);
        return "coupon/couponList";
    }

    /**
     * 쿠폰 삭제
     */
    @DeleteMapping("/coupon/{couponId}/delete")
    public String deleteCoupon(@PathVariable("couponId") Long couponId) throws Exception {
        couponService.deleteCoupon(couponId);
        return "redirect:/coupon";
    }
}
