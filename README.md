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
