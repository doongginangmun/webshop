package com.toy.webshop.repository;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ItemSearchCondition {

    @NotBlank
    private String name;
}
