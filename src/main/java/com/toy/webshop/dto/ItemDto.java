package com.toy.webshop.dto;

import com.toy.webshop.entity.item.Item;
import lombok.Data;

import java.util.List;

@Data
public class ItemDto {

    private Long id;
    private String name;
    private int price;
    private int count;
    private List<ItemImgDto> itemImgs;

    public ItemDto() {
    }

    public ItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.count = 1;
    }

    public ItemDto(Long id, String name, int price, int count, List<ItemImgDto> itemImgs) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.itemImgs = itemImgs;
    }
}
