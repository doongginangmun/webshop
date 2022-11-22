package com.toy.webshop.dto;

import com.toy.webshop.entity.CartItem;
import com.toy.webshop.entity.item.Item;
import lombok.Data;

import java.util.List;

@Data
public class ItemDto {

    private Long id;
    private String name;
    private int price;
    private int count;
    private String author;
    private String isbn;
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

    public ItemDto(Long id, String name, int price, int count,String author, String isbn, List<ItemImgDto> itemImgs) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.author = author;
        this.isbn = isbn;
        this.itemImgs = itemImgs;
    }
    public ItemDto(Long id, String name, int price, int count,String author, String isbn) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.author = author;
        this.isbn = isbn;
    }

    public ItemDto(Long id, String name, int price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }
}

