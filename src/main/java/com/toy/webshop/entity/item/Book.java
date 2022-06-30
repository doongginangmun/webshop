package com.toy.webshop.entity.item;

import com.toy.webshop.form.BookUpdateForm;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("B")
public class Book extends Item{

    private String author;
    private String isbn;

    protected Book() {}

    public void updateBook(BookUpdateForm form) {
        setName(form.getName());
        setPrice(form.getPrice());
        setStockQuantity(form.getStockQuantity());
        this.author = form.getAuthor();
        this.isbn = form.getIsbn();
    }
    @Builder
    public Book(Long id, String name, int price, int stockQuantity, String author, String isbn) {
        setId(id);
        setName(name);
        setPrice(price);
        setStockQuantity(stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }
}
