package com.toy.webshop.entity.item;

import com.toy.webshop.entity.Category;
import com.toy.webshop.entity.ItemImg;
import com.toy.webshop.entity.Order;
import com.toy.webshop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속관계의 테이블 전략지정
@DiscriminatorColumn(name = "dtype")
@ToString
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @BatchSize(size = 5)
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL) //여기서 값이 변경된다해도 적용되지않는다 즉, 읽기전용
    private List<ItemImg> itemImgs = new ArrayList<>();

    @BatchSize(size = 5)
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //비즈니스 로직
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
