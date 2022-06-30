package com.toy.webshop.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDto {

    private List<Long> chk;
    private Long couponId;
}
