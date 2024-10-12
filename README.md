# SPRING PLUS

## 리펙토링 기간

> 기간 2024.09.26 ~ 2024.10.11

<br>

## 🔧 코드 개선 
### 1️⃣ LEVEL  1 - 1 @Transactional 에러 해결
- 문제: 할 일 저장 API(/todos)를 호출할 때, Connection is read-only 에러가 발생함.
- 해결: @Transactional 어노테이션의 readOnly = true 설정을 readOnly = false로 변경하여 트랜잭션이 쓰기 작업을 처리할 수 있도록 수정함.

### 2️⃣ LEVEL 1 - 2 JWT에 유저 닉네임 추가
- 문제: User 테이블에 nickname 필드가 없고, JWT에서 닉네임을 추출할 수 없는 상태.
- 해결: User 엔티티에 nickname 컬럼을 추가하고, JWT 토큰 생성 시 닉네임을 포함하도록 수정함.
### 3️⃣ LEVEL 1 - 3 AOP 로직 개선
- 문제: changeUserRole() 메소드가 실행 전 AOP가 동작하지 않음.
- 해결: @Before 어노테이션을 사용하여 메소드 실행 전에 로그가 남도록 AOP 로직을 수정함.
### 4️⃣ LEVEL 1 - 4 컨트롤러 테스트 수정
- 문제: todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() 테스트가 실패함.
- 해결: 예외 처리 로직을 추가하고, 테스트 코드에서 예상되는 예외 상황을 반영하여 수정함.
### 5️⃣ LEVEL 1 - 5 JPA 검색 기능 확장
- 문제: weather 조건 및 수정일 기준으로 할 일 검색이 불가능한 상태.
- 해결: JPQL을 사용하여 weather 조건과 수정일 기준으로 검색할 수 있도록 메소드를 수정함.
---
### 6️⃣ LEVEL 2 - 1 JPA Cascade 기능 적용
- 문제: 할 일을 저장할 때, 유저가 담당자로 자동 등록되지 않음.
- 해결: JPA의 cascade 기능을 사용하여 할 일을 저장할 때 유저가 자동으로 담당자로 등록되도록 수정함.
### 7️⃣ LEVEL 2 - 2 N+1 문제 해결
- 문제: CommentController 클래스의 getComments() API에서 N+1 문제가 발생하여 데이터베이스 쿼리 성능이 저하됨.
- 해결: 연관된 엔티티를 한 번에 조회하는 방식으로 변경하여 N+1 문제를 해결함.
### 8️⃣ LEVEL 2 - 3 QueryDSL로 JPQL 변경
- 문제: findByIdWithUser 메소드에서 JPQL을 사용하며 N+1 문제가 발생함.
- 해결: QueryDSL로 변경하여 동적 쿼리를 작성하고 N+1 문제를 해결함.
### 9️⃣ LEVEL 2 - 4 Spring Security 도입
- 문제: 기존의 Filter와 Argument Resolver를 사용한 권한 관리 방식이 복잡함.
- 해결: Spring Security를 도입하여 접근 권한 및 유저 권한 관리를 단순화하고, JWT를 통한 인증 방식은 유지함.
---
### 1️⃣0️⃣ LEVEL 3 - 1 QueryDSL로 일정 검색 기능 구현
- 문제: 일정 검색 기능에서 성능 및 사용성에 문제가 있음.
- 해결: QueryDSL을 활용해 일정 제목, 생성일, 담당자 닉네임으로 검색할 수 있는 기능을 구현하고, Projections를 통해 필요한 필드만 반환하도록 최적화함.
### 1️⃣1️⃣ LEVEL 3 - 2 Transaction 심화 기능
- 문제: 매니저 등록 시 로그 기록이 제대로 처리되지 않음.
- 해결: @Transactional 옵션을 활용해 매니저 등록과 로그 기록이 각각 독립적으로 처리되도록 수정함.
### 1️⃣2️⃣ LEVEL 3 - 3 AWS 활용
- 문제: AWS EC2, RDS, S3를 사용한 배포 및 관리를 설정해야 함.
- 해결: EC2 인스턴스를 활용해 어플리케이션을 실행하고, RDS와 연결하여 데이터베이스를 구축하고, S3를 활용해 유저 프로필 이미지를 관리하는 API를 구현함.
### 1️⃣3️⃣ LEVEL 3 - 4 대용량 데이터 처리
- 문제: 대용량 데이터 처리 성능이 부족함.
- 해결: 테스트 코드로 유저 데이터를 100만 건을 생성하고, 닉네임 검색 속도를 개선하기 위해 여러 방법을 적용해 성능을 향상시킴.

<br>

---

## LV3 AWS
### S3로 이미지 업로드, 삭제, 및 버킷 정책

- 업로드
![image](https://github.com/user-attachments/assets/66c8afae-fa26-4da9-9e0b-c683fcc8fcdf)
![image](https://github.com/user-attachments/assets/021662c5-5281-4604-86fd-bb9e717f362f)

- 조회
![image](https://github.com/user-attachments/assets/1793277d-5509-425a-9f88-f9fa3c263f7c)


- 삭제
![image](https://github.com/user-attachments/assets/e5b4f9a2-64e1-47fd-923f-2350dce38bac)
![image](https://github.com/user-attachments/assets/75810f59-4c94-4db1-89e0-ffa0b91ac079)



- 정책
![image](https://github.com/user-attachments/assets/441dcf52-5087-4499-b470-d5276c763d39)


---


## LV3. 대용량 데이터 조회

### 방법

1. 인덱스 미적용 findByNickname (JPA)
2. 인덱스 적용 findByNickname (JPA)
3. 인덱스 적용 findByNickname (JPQL)
4. redis 적용 findByNickname (JPQL)

### 결과
인덱스 적용시 응답 속도가 향상되었습니다.

#### 성능 개선 결과

| 조회 방법   | 인덱스 미적용 조회 속도 (ms) | 인덱스 적용 후 JPA 조회 속도 (ms) | 인덱스 적용 후 JPQL 조회 속도 (ms) | redis 적용 후 JPQL 조회 속도 (ms) |
|---------|--------------------|---------------------|---------------------|---------------------|
| 유저 닉네임 조회  | 489                | 24                 |20                 |9                 |


#### 인덱스 미적용 조회
![아무것도 적용안한 닉네임 찾기](https://github.com/user-attachments/assets/5c154ff1-b8cd-4840-97f7-4253fb907a5d)

#### 인덱스 적용 JPA 조회
![JPA DB 인덱스 적용 후](https://github.com/user-attachments/assets/d351ea44-5cda-4cba-a34f-8c7531de664f)


#### 인덱스 적용 JPQL 조회
![JDBC까지 적용한 후](https://github.com/user-attachments/assets/a8d2a5e2-a829-4c2f-ba86-13d55a0f6826)


#### redis 적용 JPQL 조회
![redis 까지 적용한 후](https://github.com/user-attachments/assets/711b3c4b-a2d0-4d1b-a929-5bd301baa605)
