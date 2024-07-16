# 등록된 농장 예약가능한 날짜에 사용자 매칭 사이트 (WWOOF KOREA Website)

**1. 개발 환경 Tech Stack**
   - SpringBoot 2.1.7
   - JPA
   - MySQL
     
</br></br>**2. 서비스 로직 MVC Pattern**
     ![asas](https://github.com/user-attachments/assets/93573b7b-c6db-4b59-a5b3-d882a81c912b)
  - DTO Builder Pattern -> API 결과 값으로 build() 해서 return</br>
    WHY? Original Entity 은닉, DTO의 역할 충실
  - Service Strategy Pattern</br>
    WHY? 향후 다른 전략들의 확장 가능성 염두
  - Adapter Pattern</br>
    WHY? 해당 모듈에 타 모듈의 인터페이스를 DI로 주입받아서 사용함 

</br></br>**3. 데이터 모델 ERD**
![20240716_133934](https://github.com/user-attachments/assets/12ec3290-31b2-4f31-9df0-4f57a9499f50)


</br></br>**4. 모듈별 기능**
  </br>4-1. 로그인 회원가입 Login/Join (Spring Security)
   ![Untitled](https://github.com/user-attachments/assets/ff8b1a1e-f152-4449-b1b7-a974af7bca09)
   - JWT Token
   - Oauth (Goole, Kakao)

  </br>4-2. JPA 활용
    - FK를 PK로 활용
    - 객체에 2개의 PK 활용 (Host Img - @EqualsAndHashCode / @IdClass)
    - 검색 동적 쿼리 
    
    
    @Query("SELECT h " +
            "FROM Host h " +
            "WHERE (:farmsts is null or h.farmsts = :farmsts) " +
            "AND (:gender is null or h.gender = :gender) " +
            "And (:region is null or h.region = :region)  " +
            "AND h.maxPpl >= :reqPpl  "+
            "AND h.apprvYn = :apprvYn")

    List<Host> hostListByOptions( @Param("farmsts") String farmsts,
                                    @Param("gender") String gender,
                                    @Param("region") String region,
                                    @Param("reqPpl") int reqPpl,
                                    @Param("apprvYn") String apprvYn);
 
 
