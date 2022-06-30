package com.toy.webshop.form;

import com.toy.webshop.entity.item.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateForm {

    private Long id;

    @NotEmpty
    private String name;

    @Range(min = 1000, max = 1000000)
    @PositiveOrZero(message = "0이상의 수만 입력하세요")
    private int price;

    @Range(min = 10, max = 9999)
    @PositiveOrZero(message = "0이상의 수만 입력하세요")
    private int stockQuantity;

    private String author;
    private String isbn;

    private List<MultipartFile> itemImgList = new ArrayList<>();

    public Book toEntity() {
        return Book.builder()
                .id(id)
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .author(author)
                .isbn(isbn)
                .build();
    }
}
