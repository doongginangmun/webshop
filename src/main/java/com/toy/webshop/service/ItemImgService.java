package com.toy.webshop.service;


import com.toy.webshop.dto.ItemImgDto;
import com.toy.webshop.entity.ItemImg;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {


    private final AwsS3Service awsS3Service;
    private final ItemImgRepository itemImgRepository;

//    @Value("${spring.servlet.multipart.location}")
//    private String fileDir;
//    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile imgFile) {
        String oriImgName = imgFile.getOriginalFilename();
        String imgName = "";
        String path = "";

        if(StringUtils.hasText(oriImgName)) {
            path = awsS3Service.uploadFile(imgFile);
            imgName = path.substring(path.lastIndexOf("/")+1);

            itemImg.setImgUrl(path);
            itemImg.setImgName(imgName);
            itemImg.setOriImgName(oriImgName);
            itemImgRepository.save(itemImg);
        }
    }

    public void deleteItemImg(Long itemId) throws Exception {
        List<ItemImg> findItemImgs = itemImgRepository.findByItemId(itemId);
        if(findItemImgs.isEmpty())
            return;
        for(int i=0; i<findItemImgs.size(); i++) {
            log.info("getImgName={}", findItemImgs.get(i).getImgName());
            awsS3Service.deleteImage(findItemImgs.get(i).getImgName());
        }
    }

    public void updateItemImg(Item findItem, MultipartFile imgFile) throws Exception {
        String oriImgName = imgFile.getOriginalFilename();
        if(StringUtils.hasText(oriImgName)) {
            //기존 이미지 삭제
            deleteItemImg(findItem.getId());
            //새로운 이미지파일 s3에 저장
            String path = awsS3Service.uploadFile(imgFile);
            String imgName = path.substring(path.lastIndexOf("/") + 1);

            //db에 새로운 값 update
            List<ItemImg> findImgs = findItem.getItemImgs();
            for (int i = 0; i < findImgs.size(); i++)
                findImgs.get(i).updateItemImg(oriImgName, imgName, path);
        }
    }

//    public void deleteItemImg(Long itemId) throws Exception {
//        ItemImg savedItemImg = itemImgRepository.findById(itemId)
//                .orElseThrow(NotExistItemException::new);
//
//        deleteFileImg(savedItemImg);
//    }
//
//    private void deleteFileImg(ItemImg savedItemImg) throws Exception {
//        fileService.deleteFile(fileDir + "/" +
//                savedItemImg.getImgName());
//    }
//
//    public void updateItemImg(Item item, MultipartFile itemImgFile) throws Exception {
//        if(!itemImgFile.isEmpty()) {
//            ItemImg savedItemImg = itemImgRepository.findById(item.getId())
//                    .orElseThrow(NotExistItemException::new);
//
//            //기존 이미지 삭제
//            if (StringUtils.hasText(savedItemImg.getImgName())) {
//                deleteFileImg(savedItemImg);
//            }
//
//            String oriImgName = itemImgFile.getOriginalFilename();
//            String imgName = fileService.uploadFile(fileDir, oriImgName, itemImgFile.getBytes());
//            String imgUrl = "/images/item/" + imgName;
//            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
//        }
//    }
//    public void saveItemImg(ItemImg itemImg, MultipartFile imgFile) throws Exception {
//
//        String oriImgName = imgFile.getOriginalFilename();
//        String imgName = "";
//        String imgUrl = "";
//
//        if(StringUtils.hasText(oriImgName)) {
//            imgName = fileService.uploadFile(fileDir, oriImgName, imgFile.getBytes());
//            imgUrl = "/images/item/" + imgName;
//        }
//        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
//        itemImgRepository.save(itemImg);
//    }

    //아이템 이미지 Dto 변환
    public List<ItemImgDto> findItemImgs(Long itemId) {
        List<ItemImg> findItemImgs = itemImgRepository.findByItemId(itemId);
        return findItemImgs.stream()
                .map(ItemImgDto::new).collect(Collectors.toList());
    }

}
