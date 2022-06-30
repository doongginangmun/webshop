package com.toy.webshop.controlloer;

import com.toy.webshop.config.Login;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.coupon.UserCoupon;
import com.toy.webshop.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    /**
     * 쿠폰 발급 받기
     */
    @PostMapping("/coupon/{couponId}/issued")
    public String issuedCoupon(@PathVariable("couponId") Long couponId,
                               @Login User user,
                               RedirectAttributes redirectAttributes) {
        boolean duplicate = userCouponService.validateDuplicateCoupon(user.getId(), couponId);
        if(duplicate) {
            redirectAttributes.addFlashAttribute("msg", "이미 발급 받은 쿠폰입니다.");
            return "redirect:/coupons";
        }
        userCouponService.save(user, couponId);
        return "coupon/myCoupon";
    }

    /**
     * 쿠폰함
     */
    @GetMapping("/usercoupons")
    public String userCoupon(@Login User user, Model model) {
        List<UserCoupon> myCoupon = userCouponService.findMyCoupon(user);
        model.addAttribute("myCoupons", myCoupon);
        return "coupon/myCoupon";
    }
}
