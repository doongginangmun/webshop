# Webshop
## 미니 쇼핑몰 개인 프로젝트
 다양한 기술들을 공부하면서 `한번 직접 만들어 보자!` 라고 생각해서 프로젝트를 시작했습니다.
 </br>
 앞으로도 배우거나 추가하고싶은 기능이 생긴다면 지속적으로 업데이트할 예정이고,
 </br>
 css 부분은 최대한 간소화 하여 기능구현에 중점들 두었습니다.
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

## 구현 기능
* 계정
  * 회원가입, 로그인, remember-me 쿠키
* 장바구니 구현
  * 목록에서 선택주문, 선택 삭제
* 아이템
  * CRUD, 이름으로 검색, 가격 내림차순 정렬, 페이징 처리
  * 아이템 이미지 aws s3에 저장
* 주문
  * CRUD 
* 쿠폰
  * 고정 or 비율 할인쿠폰 CRUD

## 배포
배포는 aws ec2 환경에 직접 배포하였습니다.
ec2 주소는 포트폴리오 작품 소개란에 있습니다.
