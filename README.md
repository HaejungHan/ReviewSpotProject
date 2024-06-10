# 🚩 ReviewSpot : 문화컨텐츠 리뷰 프로그램
- 내용 : 다양한 문화 콘텐츠에 대한 리뷰를 공유할 수 있는 특화된 블로그 서비스입니다. <br>
이 플랫폼을 통해 사용자들은 자신의 경험을 생생하게 기록하고, 다른 사람들과 소통하며, 유익한 정보를 나눌 수 있습니다.<br>
- 기획의도 : 플랫폼을 통해서 자신의 문화 컨텐츠 경험에 대해 리뷰를 하고 다른 사람들과 소통할 수 있는 창구를 만들기 위해 기획하였습니다.<br>
<br>

## 📌 팀원소개 & 역할분담
- 최지연(리더) : 게시물 (작성,선택조회,전체조회,삭제,수정)
- 박강현(팀원) : 회원가입,회원탈퇴,로그인,로그아웃, 댓글(등록,수정,삭제)
- 정연주(팀원) : 게시물 (작성,선택조회,전체조회,삭제,수정)
- 한승훈(팀원) : 프로필 (조회,수정)
- 한해정(팀원) : 프로필 (조회,수정) , 게시물/댓글 좋아요, 이메일 인증 및 회원가입
  ![image](https://github.com/pkhyun/ReviewSpotProject/assets/130989670/b520e1fc-9840-4a51-83e8-8cde5613b441)

<br>

## 💡 Tech Stack
- 언어 : Java
- 버전 : JDK17
- Tools : GitHub, Git
- IDE : IntelliJ IDEA
- DB: MySQL 8.0.37
- Framework : SpringBoot 3.2.5
- 인증/인가 방식 : JWT
<br>

## 🚩 프로젝트 소개
<details>
<summary> 👀 요구 사항 </summary> 
<br>
<br>

1️⃣ 사용자 인증 기능 <br>
  - 회원 가입(사용자 ID, 비밀번호) <br>
    - 사용자 ID : 중복, 탈퇴한 ID X / 대소문자 포함 영문 + 숫자만, 10글자 ~ 20글자 <br>
    - 비밀번호 : 대소문자 포함 영문 + 숫자 + 특수문자 최소 1글자씩 포함 / 최소 10글자 이상 <br>
    - 예외 : 중복된 ID , 비밀번호 형식이 올바르지 않은 경우 <br>
  - 회원 탈퇴 <br>
    - 탈퇴한 ID 재사용, 복구 X / 재탈퇴 처리 불가 <br>
    - 예외 : ID와 비밀번호 일치 X / 이미 탈퇴한 ID <br>
  - 로그인 <br>
    - 클라이언트에게 토큰 발행(Access Token : 30분, Refresh Token : 2주) <br>
    - 회원가입된 ID와 비밀번호가 일치할 경우 <br>
    - 성공 시, header에 토큰 추가 후 성공 상태코드와 메세지 반환 <br>
  - 탈퇴 or 로그아웃 -> Refresh Token 유효X <br>
     - 예외 : 유효하지 않은 사용자 정보로 로그인 시도 / ID와 비밀번호 일치X <br>
  - 로그 아웃 <br>
    - 발행한 토큰 초기화 / 초기화된 Refresh Token 재사용X, 재로그인해야 함 <br>

2️⃣ 프로필 관리 기능 <br>
  - 프로필 조회 : 사용자 ID, 이름, 한 줄 소개, 이메일 / ID(사용자 ID X), 비밀번호, 생성일자, 수정일자 데이터 노출 X <br>
  - 프로필 수정 <br>
      - 비밀번호 수정 <br>
      - 현재 비밀번호 입력 후 올바른 경우에만 수정 가능 <br>
      - 현재 비밀번호와 동일한 비밀번호로 변경 X <br>
      - 예외 : 현재 비밀번호가 일치 X / 비밀번호 형식이 올바르지 X /현재 비밀번호와 동일한 비밀번호로 수정<br>

3️⃣ 뉴스피드 게시물 CRUD 기능 <br>
  - 게시물 작성, (선택)조회, 수정, 삭제<br>
      - 작성, 수정, 삭제는 인가가 필요 / 유요한 JWT 토큰을 가진 작성자 본인만 처리 가능<br>
      - 예외 : 작성자 이외 게시물 작성, 수정, 삭제를 시도할 경우<br>
  - 게시물 전체 조회<br>
      - 모든 사용자가 데이터 조회 가능<br>
      - 기본 정렬은 생성일자 기준으로 최신순으로<br>
      - 뉴스피트가 없는 경우<br>

4️⃣ 댓글 CRUD 기능 <br>
  - 댓글 작성, 조회, 수정, 삭제 기능 <br>
      - 작성, 수정(내용만 수정가능), 삭제는 수정/삭제는 인가가 필요 / 유요한 JWT 토큰을 가진 작성자 본인만 처리 가능<br>
      - 예외 : 작성자 이외 게시물 작성, 수정, 삭제를 시도할 경우<br>
  - 게시물 전체 조회<br>
      - 모든 사용자가 데이터 조회 가능<br>
      - 기본 정렬은 생성일자 기준으로 최신순으로<br>
      - 뉴스피트가 없는 경우<br>

5️⃣ 이메일 가입 및 인증 기능 <br>
  - 사용자가 가입한 이메일 주소로 인증번호 발송<br>
  - 발송한 인증번호와 입력란의 인증번호가 일치하는 지 확인<br>
  - 이메일 인증이 완료되지 않은 회원들의 회원상태코드를 ‘인증 전’ 으로 설정<br>

6️⃣ 좋아요 기능 <br>
  - 사용자가 게시물이나 댓글에 좋아요를 남기거나 취소가능<br>
  - 본인이 작성한 게시물과 댓글에 좋아요 불가능<br>
  - 같은 게시물에는 사용자당 한 번만 좋아요가 가능<br>

7️⃣ Swagger 적용 <br>
  - localhost:8080/swagger-ui/index.html  주소로 접근시 접속이 가능해야함<br>

  <br>
</details>

## 🎈 와이어프레임
[와이어프레임](https://www.notion.so/teamsparta/5-2b650ebf5b8748239194a293b514b60e?pvs=4#265166efb66146d09ccb77de41c2a885)


## 💡 Git branch 사용
[브랜치 활용](https://www.notion.so/teamsparta/5-2b650ebf5b8748239194a293b514b60e?pvs=4#a2dd70c7eae448f2b79e8b9626a81e2f)

## ⚙ API 명세서
[API 명세서](https://www.notion.so/teamsparta/743348a021934b2481c666597fb4f9df?v=df96765736894f9e97dc7dc72ea49feb&pvs=4)

## 📈 ERD 다이어그램
[ERD다이어그램](https://www.notion.so/teamsparta/5-2b650ebf5b8748239194a293b514b60e?pvs=4#1133c31ac86c4fa684c5220af9f752dd)

## 🎇 트러블슈팅
<details>
<summary> 기획 설계에 대한 문제💦 </summary> 
<br>
1️⃣ 문제 발견 <br>
-  초기 개발 단계에서 각자가 맡은 부분을 각자의 해석에 따라 개발하였습니다. <br>

2️⃣ 문제 해결 <br>
- 기능 개발 이후, 팀 회의에서 코드 리뷰를 진행하는 과정에서 기존 프로필 조회 부분에 대한 해석이 달랐음을 확인하였고, 회의를 통해 기능의 방향을 수정하였습니다.<br>

3️⃣ 향후 방안 <br>
- 앞으로 팀 프로젝트를 진행할 때, 각 기능의 설계에 대해 사전에 충분한 소통이 필요하다는 것을 느꼈습니다. <br>
  <br>
</details>

## 🎉 회고




