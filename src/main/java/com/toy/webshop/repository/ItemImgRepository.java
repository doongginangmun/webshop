package com.toy.webshop.repository;

import com.toy.webshop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long id);
    List<ItemImg> findByItemId(Long itemId);
}
