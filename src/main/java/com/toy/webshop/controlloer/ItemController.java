package com.toy.webshop.controlloer;

import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.entity.item.Book;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.form.BookForm;
import com.toy.webshop.form.BookUpdateForm;
import com.toy.webshop.repository.ItemSearchCondition;
import com.toy.webshop.service.ItemService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품 등록 폼 전송
     */
    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    /**
     * 상품 등록
     * @param form - createItemForm.html 에서 보낸 저장할 Entity data
     * @param bindingResult - form 검증하고 오류를 담는 객체
     */
    @PostMapping("/items/new")
    public String createItem(@Valid @ModelAttribute("form") BookForm form, BindingResult bindingResult) throws Exception {
        if(form.getItemImgList().get(0).isEmpty() || form.getItemImgList().size()==0) {
            bindingResult.rejectValue("itemImgList","ImgFileMin", "이미지 파일은 최소 한개 이상 필요합니다.");
        }
        if(bindingResult.hasErrors()) {
            return "items/createItemForm";
        }
        Item item = form.toEntity();
        itemService.saveItem(item, form.getItemImgList());
        return "redirect:/items";
    }

    /**
     * 상품 수정 폼 전송
     */
    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {

        Book item = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        form.setItemImgs(item.getItemImgs());

        model.addAttribute("form", form);

        return "items/updateItemForm";
    }

    /**
     * 상품 수정
     */
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@Valid @ModelAttribute("form") BookUpdateForm form, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "items/updateItemForm";
        }
        itemService.updateItem(form, form.getItemImgList());
        return "redirect:/items";
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/items/{itemId}/delete")
    public String deleteItem(@PathVariable("itemId") Long itemId) throws Exception {
        log.info("item delete 진입");
        itemService.delete(itemId);
        return "redirect:/items";
    }

    /**
     * 상품 목록
     */
    @GetMapping("/items")
    public String list(@PageableDefault(size = 5) Pageable pageable, Model model) {
        Page<ItemDto> items = itemService.findItems(pageable);
        model.addAttribute("items", items);
        return "items/itemList";
    }

    /**
     * 상품 동적 검색
     */
    @GetMapping("/items/search")
    public String itemSearch(@Valid @ModelAttribute("itemSearch")ItemSearchCondition condition,
                             BindingResult bindingResult, Pageable pageable,
                             Model model) {
        if(bindingResult.hasErrors())
            return "redirect:/shopping";

        log.info("ItemSearchCondition={}", condition.getName());
        Page<ItemDto> itemDtos = itemService.itemSearch(condition, pageable);
        model.addAttribute("items", itemDtos);
        return "order/orderForm";
    }
}
