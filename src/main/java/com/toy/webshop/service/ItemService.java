package com.toy.webshop.service;

import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.dto.ItemImgDto;
import com.toy.webshop.entity.ItemImg;
import com.toy.webshop.entity.item.Book;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.exception.NotExistItemException;
import com.toy.webshop.form.BookUpdateForm;
import com.toy.webshop.repository.ItemRepository;
import com.toy.webshop.repository.ItemSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemImgService itemImgService;
    private final ItemRepository itemRepository;


    /**
     * 상품 삭제
     * @param itemId
     */
    @Transactional
    public void delete(Long itemId) throws Exception {
        itemRepository.deleteById(itemId);
    }

    /**
     * 영속성 컨텍스트가 자동으로 update
     * 변경 감지
     */
    @Transactional
    public Long updateItem(BookUpdateForm item, List<MultipartFile> multipartFileList) throws Exception {
        //상품 수정
        Book findItem =(Book) itemRepository.findById(item.getId())
                .orElseThrow(NotExistItemException::new);
        findItem.updateBook(item);
        
        //이미지 수정
        for(int i=0; i<multipartFileList.size(); i++) {
            itemImgService.updateItemImg(findItem, multipartFileList.get(i));
        }
        return findItem.getId();
    }

    @Transactional
    public Long saveItem(Item item, List<MultipartFile> multipartFileList) throws Exception {
        itemRepository.save(item);

       for(int i=0; i< multipartFileList.size(); i++) {
           ItemImg itemImg = new ItemImg();
           itemImg.setItemImg(item);

           if(i == 0)
               itemImg.setRepImgUrl("Y");
           else
               itemImg.setRepImgUrl("N");

           itemImgService.saveItemImg(itemImg, multipartFileList.get(i));
       }
        return item.getId();
    }

    @Transactional
    public Page<ItemDto> findItems(Pageable pageable) {
        Page<Item> items = itemRepository.items(pageable);
        //dto 변환
        return items.map(i-> new ItemDto(i.getId(), i.getName(), i.getPrice(), i.getStockQuantity(),
                i.getItemImgs().stream().map(ItemImgDto::new).collect(Collectors.toList())));
    }

    public Page<ItemDto> itemSearch(ItemSearchCondition condition, Pageable pageable) {
       return itemRepository.dynamicSearchItems(condition, pageable);
    }

    public Item findOne(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(NotExistItemException::new);
    }

    public List<Item> findPurchaseItems(List<Long> itemIds) {
        return itemRepository.findPurchaseItems(itemIds);
    }
}
