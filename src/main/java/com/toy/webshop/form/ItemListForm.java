package com.toy.webshop.form;

import com.toy.webshop.entity.ItemImg;
import com.toy.webshop.entity.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemListForm {

    private Long id;
    private int price;
    private String name;
    private List<ItemImg> itemImgs = new ArrayList<>();

    public ItemListForm(Item item) {
        this.id = item.getId();
        this.price = item.getPrice();
        this.name = item.getName();
        this.itemImgs = item.getItemImgs();
    }
}
