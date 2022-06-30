package com.toy.webshop.form;

import com.toy.webshop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgForm {

    private Long id;
    private String imgName; //이미지 파일명
    private String oriImgName; //원본 파일명
    private String imgUrl;  //이미지 조회 경로
    private String repImgUrl; //대표 이미지 여부

    public ItemImg toEntity() {
        return ItemImg.builder()
                .imgName(imgName)
                .oriImgName(oriImgName)
                .imgUrl(imgUrl)
                .repImgUrl(repImgUrl)
                .build();
    }

}
