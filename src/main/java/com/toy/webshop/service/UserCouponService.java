package com.toy.webshop.service;

import com.toy.webshop.entity.User;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.entity.coupon.UserCoupon;
import com.toy.webshop.exception.NotExistItemException;
import com.toy.webshop.exception.NotFoundUserException;
import com.toy.webshop.repository.CouponRepository;
import com.toy.webshop.repository.UserCouponRepository;
import com.toy.webshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(User user, Long couponId) {

        Coupon findCoupon = couponRepository.findById(couponId)
                .orElseThrow(NotExistItemException::new);
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(NotFoundUserException::new);

        UserCoupon userCoupon = UserCoupon.createUserCoupon(findUser, findCoupon, 1);

        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);
        return savedUserCoupon.getId();
    }

    public boolean validateDuplicateCoupon(Long userId, Long couponId) {
        return userCouponRepository.existsByUserIdAndCouponId(userId, couponId);
    }

    public List<UserCoupon> findMyCoupon(User user) {

        List<UserCoupon> findMyCoupon = userCouponRepository.findByUserId(user.getId());

        return findMyCoupon.stream()
                .filter(c -> c.getQuantity() != 0 && !c.isUsed())
                .collect(Collectors.toList());
    }
}
