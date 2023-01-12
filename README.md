# :cake:개발하는 커비:cake:
<img src="https://img.shields.io/badge/Javascript-F7DF1E?style=flat-square&logo=Javascript&logoColor=white"/> <img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=React&logoColor=white"/> <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white"/> <img src="https://img.shields.io/badge/Sass-CC6699?style=flat-square&logo=Sass&logoColor=white"/> <img src="https://img.shields.io/badge/Apache Tomcat-F8DC75?style=flat-square&logo=Apache Tomcat&logoColor=white"/> <img src="https://img.shields.io/badge/Oracle-F80000?style=flat-square&logo=Oracle&logoColor=white"/> <img src="https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=white"/> <img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=flat-square&logo=Visual Studio Code&logoColor=white"/> <img src="https://img.shields.io/badge/Eclipse IDE-2C2255?style=flat-square&logo=Eclipse IDE&logoColor=white"/> 

> 개발자들의 정보 공유를 위한 게시판  



## :cherries: 목차

- [프로젝트 소개](#프로젝트-소개)
- [프로젝트 주요 기능](#프로젝트-주요-기능)
- [개발 기간](#개발-기간)
- [개발 환경](#개발-환경)
- [업데이트 내역](#업데이트-내역)

### :cherries: 프로젝트 소개
개발하는 커비는 커비처럼 다양한 정보 흡수하겠다는 의미로,
개발자들의 다양한 정보 공유를 위한 게시판입니다.



![](../header.png)

### :cherries: 프로젝트 주요 기능
- 모바일과 아이패드 환경에서 이용 가능한 미디어 쿼리 적용
- 사용자 편의를 위한 게시글 무한 스크롤 구현
- 메인 페이지에서 각 게시판의 최신 게시글, 주간 인기글 그리고 실시간 댓글이 달린 게시글 확인 가능
- 회원가입한 회원만 메인 페이지 이용 가능
- 희망 회원 프로필 사진 업로드 가능
- 원활한 게시글 관리를 위한 게시판마다 게시글의 수 조회와 최신 글 5개 조회 가능
- Admin 편리하게 회원과 게시판, 게시글, 댓글, 배너 등을 관리
- Admin 페이지에서 광고 수신 동의 회원을 위한 광고메일 발송

### :cherries: 팀원
- [:rabbit2:신은지](https://github.com/eunjishinrabbit)
- [:dolphin:이희주](https://github.com/brownsally2)
- [:hatched_chick:이한솔](https://github.com/solsol529)

### :cherries: 개발 기간
- 기획 및 개발 : 2022.10.19 - 2022.11.10

### :cherries: 개발 환경
- OS : Window 10
- IDE : VS Code, Eclipse
- Language : Java 11, Javascript ES6
- FrontEnd : HTML/CSS
- Library : ReactJS
- DB : Oracle
- Server : Tomcat

### :cherries: 업데이트 내역
- 0.0.0 (2022.11.10)
  - 작업 1차 완성
  

### :cherries: 빌드 전 사전 작업
- dk_admin_front api/firebase firebaseconfig 의 firebase api 인증 정보 입력
- Dk_front_v2 api/firebase firebaseconfig 의 firebase api 인증 정보 입력
- Kirby_Back Admin/EmailSender sendSimple 의 java mail api용 메일 계정 정보 입력 
- Kirby_Back Main/sens_sms sendSms 의 네이버 클라우드 플랫폼 api 인증 정보 입력

### :cherries: 빌드

```sh
yarn install
```
```sh
yarn start
```
Kirby_Back : 이클립스를 통해 파일 실행  
Dk_front_v2, dk_admin_front : 각 폴더를 위의 명령어들로 파일 실행
