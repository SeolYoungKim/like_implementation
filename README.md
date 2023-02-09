# 김설영 
## 프로젝트에 사용된 기술 스택
- Gradle 7.6
- Java 1.8
- Spring Boot 2.7.8
- Spring Data JPA 2.7.7
- Query DSL 5.0
- Spring Security 5.7.6

## 프로젝트 구조
```text
.
└── community_service
    ├── CommunityServiceApplication.java
    ├── auth
    │   ├── AuthenticationToken.java
    │   ├── CustomAuthentication.java
    │   ├── CustomAuthenticationFilter.java
    │   └── SecurityConfig.java
    ├── config
    │   ├── JpaConfig.java
    │   └── QueryDslConfig.java
    ├── exception
    │   └── presentation
    │       ├── ExceptionController.java
    │       └── dto
    ├── like
    │   ├── application
    │   │   └── LikesService.java
    │   ├── domain
    │   │   ├── LikeStatus.java
    │   │   ├── Likes.java
    │   │   └── LikesRepository.java
    │   └── presentation
    │       └── LikesController.java
    ├── member
    │   └── domain
    │       ├── AccountType.java
    │       ├── Member.java
    │       ├── MemberRepository.java
    │       └── Quit.java
    ├── post
    │   ├── application
    │   │   ├── PostService.java
    │   │   └── dto
    │   ├── domain
    │   │   ├── Delete.java
    │   │   ├── Post.java
    │   │   ├── PostQueryRepository.java
    │   │   └── PostRepository.java
    │   └── presentation
    │       ├── PostController.java
    │       └── dto
    └── test_data
        └── TestData.java
```

## 실행 방법
### [1. CommunityServiceApplication의 main() 메서드 위치](src/main/java/com/kimsy/community_service/CommunityServiceApplication.java)
### [2. Test Data 정보 위치](src/main/java/com/kimsy/community_service/test_data/TestData.java) 및 요약 
- 회원 토큰 (총 3개) 
  - Realtor 1
  - Lessor 2
  - Lessee 3
- 각 회원당 작성한 게시글 1개 (총 3개)
- 각 회원이 누른 좋아요의 구성이 조금씩 다르게 구성되어 있음 

### 3. 게시글 작성
#### 회원이 게시글 작성
```bash
curl --location --request POST 'http://localhost:8080/api/posts' \
--header 'Authentication: Realtor 1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "title":"title",
    "contents":"contents"
}'
```

#### 회원이 아닌 사람이 게시글 작성
```bash
curl --location --request POST 'http://localhost:8080/api/posts' \
--header 'Content-Type: application/json' \
--data-raw '{
    "title":"title",
    "contents":"contents"
}'
```

### 4. 게시글 수정
#### 작성자가 게시글 수정
```bash
curl --location --request PUT 'http://localhost:8080/api/posts/1' \
--header 'Authentication: Realtor 1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "title":"중개사가 제목을 수정",
    "contents":"중개사가 내용을 수정"
}'
```

#### 작성자가 아닌 사람이 게시글 수정
```bash
curl --location --request PUT 'http://localhost:8080/api/posts/1' \
--header 'Authentication: Lessor 2' \
--header 'Content-Type: application/json' \
--data-raw '{
    "title":"중개사가 제목을 수정",
    "contents":"중개사가 내용을 수정"
}'
```

#### 회원이 아닌 사람이 게시글 수정
```bash
curl --location --request PUT 'http://localhost:8080/api/posts/1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "title":"중개사가 제목을 수정",
    "contents":"중개사가 내용을 수정"
}'
```

### 5. 게시글 삭제 
#### 작성자가 게시글 삭제
```bash
curl --location --request DELETE 'http://localhost:8080/api/posts/1' \
--header 'Authentication: Realtor 1'
```

#### 작성자가 아닌 사람이 게시글 삭제
```bash
curl --location --request DELETE 'http://localhost:8080/api/posts/1' \
--header 'Authentication: Lessor 2'
```

#### 회원이 아닌 게시글 삭제
```bash
curl --location --request DELETE 'http://localhost:8080/api/posts/1'
```

### 6. 게시글 단건 조회
- 단건 조회 기능은 회원이 좋아요를 눌렀는지 유무, 회원 유무에 따라 `likeStatus` 필드가 다르게 보입니다.

#### 회원이 조회
```bash
curl --location --request GET 'http://localhost:8080/api/posts/3' \
--header 'Authentication: Realtor 1'
```

#### 회원이 아닌 사람이 조회
```bash
curl --location --request GET 'http://localhost:8080/api/posts/3'
```

### 7. 게시글 여러건 조회
- 단건 조회 기능은 회원이 좋아요를 눌렀는지 유무, 회원 유무에 따라 `likeStatus` 필드가 다르게 보입니다.
- 페이징 기준은 size=10, 생성일 내림차순입니다.

#### 회원이 조회
```bash
curl --location --request GET 'http://localhost:8080/api/posts' \
--header 'Authentication: Realtor 1'
```

#### 회원이 아닌 사람이 조회
```bash
curl --location --request GET 'http://localhost:8080/api/posts'
```


### 7. 좋아요 
#### 좋아요 누르기 
```bash
curl --location --request POST 'http://localhost:8080/api/posts/1/likes' \
--header 'Authentication: Lessee 3'
```

#### 좋아요 취소하기
```bash
curl --location --request DELETE 'http://localhost:8080/api/posts/1/likes' \
--header 'Authentication: Lessee 3'
```


## 검증 및 구현 방식 
### 검증 
- 토큰 값
  - 넘어온 값을 파싱하고, `AuthenticationToken`이라는 객체를 만들어 내부에서 검증 수행 
  - 토큰 값의 일부인 `Realtor`등 계정 타입 관련 정보는 `AccountType`이라는 enum 내부에서 검증 수행 
- 테스트 코드
  - Junit5, Mockito 프레임워크를 이용한 단위 테스트를 작성하여 검증 
  - `QueryDSL`을 이용한 Repository 한정 `@SpringBootTest`를 이용해 통합 테스트를 작성하여 검증 

### 구현 방식 
1. 최우선적으로 기능이 돌아가도록 구현
   - 이 때, 기능은 단위 기능별로 구현하였습니다. (단위 기능 : 글 작성, 수정, 삭제...)
2. 중복 로직을 확인하고, 제거 (리팩토링)
   - 중복 로직을 제거하기 위한 방안을 고려하여 적용하였습니다.
3. 컨벤션 수정 
   - 컨벤션에 위반되는 로직이 있을 시 수정하였습니다. 
