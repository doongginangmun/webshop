# Webshop
## 미니 쇼핑몰 개인 프로젝트
 다양한 기술들을 공부하면서 `한번 직접 만들어 보자!` 라고 생각해서 프로젝트를 시작했습니다.
 </br>
 앞으로도 배우거나 추가하고싶은 기능이 생긴다면 지속적으로 업데이트할 예정이고,
 </br>
 css부분은 bootStrap을 사용하여 만들었습니다.
 </br>
 
## 사용한 기술스택
* Intelij
* Java 11
* Spring boot
* Spring security
* Spring data JPA
* QueryDsl
* Thymeleaf
* MySql
* Aws s3
* BootStrap

## 현재 구현 기능
* 계정
  * 회원가입, 로그인, remember-me 쿠키
* 장바구니 구현
  * 목록에서 선택주문, 선택 삭제
* 아이템
  * CRUD, 이름으로 검색, 가격 내림차순 정렬, 페이징 처리
  * 아이템 이미지 aws s3에 저장
* 주문
  * 기본주문, 쿠폰주문 CRUD
* 쿠폰
  * 고정 or 비율 할인쿠폰 CRUD
* 댓글
  * 상품상세페이지에서 댓글 CRD 구현 (ajax사용)
* 포인트
  * 포인트 적립

## 배포
배포는 aws ec2 환경에 직접 배포하였습니다.
