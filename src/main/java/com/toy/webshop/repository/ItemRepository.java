package com.toy.webshop.repository;

import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.entity.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {


    @Override
    @EntityGraph(attributePaths = {"itemImgs"})
    Page<Item> findAll(Pageable pageable);

//    @Query("select i from Item i where i.id in :itemIds")
//    List<Item> findPurchaseItems(@Param("itemIds") Collection<Long> itemIds);

    @Query("select new com.toy.webshop.dto.ItemDto(i.id, i.name, i.price, i.stockQuantity, i.author, i.isbn)" +
            " from Item i where  i.id =:itemId")
    ItemDto findBooks(@Param("itemId") Long itemId);
}
