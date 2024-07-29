# 등록된 농장 예약가능한 날짜에 사용자 매칭 사이트 (WWOOF KOREA Website)

참고 사이트 
https://wwoof.fr/en/hosts
</br>
개발 정리 페이지 
https://www.notion.so/4e48c0e35b6445a380656449666a25c1?v=95155cce16bb48f2b2735d783d85fb8b&pvs=4

#### 1. 개발 환경 Tech Stack
   - SpringBoot 2.1.7
   - JPA
   - MySQL
     
#### 2. 서비스 로직 MVC Pattern
     ![asas](https://github.com/user-attachments/assets/93573b7b-c6db-4b59-a5b3-d882a81c912b)
  - DTO Builder Pattern -> API 결과 값으로 build() 해서 return</br>
    WHY? Original Entity 은닉, DTO의 역할 충실
  - Service Strategy Pattern</br>
    WHY? 향후 다른 전략들의 확장 가능성 염두
  - Adapter Pattern</br>
    WHY? 해당 모듈에 타 모듈의 인터페이스를 DI로 주입받아서 사용함 

</br></br>####3. 데이터 모델 ERD
![20240716_133934](https://github.com/user-attachments/assets/12ec3290-31b2-4f31-9df0-4f57a9499f50)


#### 4. 모듈별 기능
  **4-1. Spring Security**
   ![Untitled](https://github.com/user-attachments/assets/ff8b1a1e-f152-4449-b1b7-a974af7bca09)
   - JWT Token
      - User → Server : Login access 
      - User ← Server : Encoding Header, Payload and return the token
      - TokenProvider : Making JWT by received user information 
           - JwtAuthenticationFilter
             ```java
             Date expiryDate = Date.from(Instant.now()
				.plus(1, ChronoUnit.DAYS));

               // JWT Token 생성
               byte[] keyBytes = SECRET_KEY.getBytes();
               Key key = Keys.hmacShaKeyFor(keyBytes);
               
               return Jwts.builder()
               		// header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
               		.signWith(key, SignatureAlgorithm.HS512)
               		// payload에 들어갈 내용
               		.setSubject(user.getId()) // userId 값
               		.setIssuer("demo app") // iss
               		.setIssuedAt(new Date()) // iat
               		.setExpiration(expiryDate) // exp
               		.compact();
              ```
   - Oauth (Goole, Kakao)
        - application properties  (id, redirect uri 설정)
        - Security Config endpoint 
          ```java
          @Override
            public void configure(WebSecurity web) throws Exception {
                web.ignoring().mvcMatchers("/image/**", "/css/**", "/js/**");    // /image/** 있는 모든 파일들은 시큐리티 적용을 무시한다.
                web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());    // 정적인 리소스들에 대해서 시큐리티 적용 무시.
            }
            => 정적 리소스들에 대해서 검증 생략 
            
            .antMatchers("/", "/auth/**", "/api/**", "/oauth2/**").permitAll()
            .anyRequest() // /와 /auth/**이외의 모든 경로는 인증 해야됨.
            .authenticated()
            .and()
            .oauth2Login()
            .redirectionEndpoint()
            .baseUri("/login/oauth2/code/*"); 으로 들어오는 요소를
            .and().userInfoEndpoint().userService(oauthUserService);
            .successHandler(oAuthSuccessHandler)
          ```

      - OauthService 파일에선 API 연결 이후 받아온 정보를 가공하여 자동으로 회원가입
        ```java
        final OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

         // getRegistrationId 현재 로그인 진행 중인 서비스를 구분하는 코드
         
         if("kakao".equals(oauthType.toLowerCase())){
         	email = ((Map<String, Object>)attributes.get("kakao_account")).get("email").toString();
         	name = ((Map<String, Object>)attributes.get("kakao_account")).get("nickname").toString();
         } else if ("google".equals(oauthType.toLowerCase())) {
         	email = attributes.get("email").toString();
         	name = attributes.get("name").toString();
         }
        ```


  </br>**4-2. Spring Security - Loing/Logout**

- password Encoding
  - password is saved by encoding 
  ```java
     User user = User.builder()
           .email(userDto.getEmail())
           .name(userDto.getName())
           .password(passwordEncoder.encode(userDto.getPassword()))
           .build();
     ```
   
</br>**4-3. Host Register (CRUD - File-image and Address Api)**

- Saving the multiple image
  - First, saving the main information and return host number
  - Second, saving the additional multiple images with the returned host number
    ```java
    // 호스트 등록 Request
      @PostMapping("/api/host/save")
      public String save(@RequestPart(value = "file") MultipartFile file, @RequestPart(value = "hostData") HostSaveRequestDto saveRequestDto) throws IOException {
          return hostsService.save(saveRequestDto, file);
      }
      
      // 호스트 이미지 등록
      @PostMapping("/api/host/saveImgs")
      public void saveImgs(@RequestPart("files") MultipartFile[] files, String hostNum ){
          hostsService.saveImgs(files, hostNum);
      }
    ```

- Updating the image
    - Finding a host information by Principal Entity
    - Calling the saved images
         - Return Entity : ResponseEntity
         - Security Config exceptional path
        ```java
        @CrossOrigin
         @GetMapping(value = "/image/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
         public ResponseEntity<?> returnImage(@PathVariable String fileName) throws Exception {
            String path = "";
            HostMainImg hostMainImg = hostMainImgRepository.findMainImg(fileName);
            if(hostMainImg!=null){
               path = hostMainImg.getFilepath(); //이미지가 저장된 위치
               Resource resource = new FileSystemResource(path);
               HttpHeaders headers = new HttpHeaders();
               Path filepath = null;
               filepath = Paths.get(path);
               headers.add("Content-Type", Files.probeContentType(filepath));
               return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else{
               HostImg hostImg = hostImgRepository.findImg(fileName);
               if(hostImg!=null){
                  path = hostImg.getFilepath();
                  Resource resource = new FileSystemResource(path);
                  HttpHeaders headers = new HttpHeaders();
                  Path filepath = null;
                  filepath = Paths.get(path);
                  headers.add("Content-Type", Files.probeContentType(filepath));
                  return new ResponseEntity<>(resource, headers, HttpStatus.OK);
               } else{
                  throw new Exception("No img");
               }
            }
         }
        ```
   
</br>**4-4. Host Searching**

- Mapping the searched datas with the entities (JPA)
  - First, finding the data from the host table (farms status, gender, region)
  - Second, finding the data from the reservation table (whether the farm is not avaliable at the date)
  - Third, among the farms with available space (deducted the occupied reserved people), finding the data from Reservation table where they have more(or same) rest space than required space
    ```java
    // first
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
      
      // second
      @Query("SELECT rd.resrvHis.host.hnum FROM ResrvDscn rd WHERE rd.resrvHis.host.hnum IN :host")
      List<Long> resrvDscnHostList(@Param("host") List<Long> hostList);
      
      
      // third 					  
      @Query("SELECT rd.resrvHis.host.hnum " +
            "FROM  ResrvDscn rd " +
            "WHERE rd.resrvHis.host.hnum IN :rdHost " +
            "AND :reqPpl > rd.restPpl " +
            "AND (rd.resrvHis.startDate < :srchEndDate OR rd.resrvHis.endDate > :srchStartDate )")
      List<Long> unAvailHostList ( @Param("srchStartDate") Date srchStartDate,
                                @Param("srchEndDate") Date srchEndDate,
                                @Param("reqPpl") int reqPpl,
                                @Param("rdHost") List<Long> resrvDscnHostList);
      ```

</br>**4-5. WishList**

- Making DTO Builder with the parameter origin WishList Entity to use stream function for list view
  ```java
  @Getter
   public class WishListResponseDto {
       private Long wishNum;
   
       @Builder
       public WishListResponseDto(WishList wishList) {
           this.wishNum = wishList.getWishNum();
       }
   
   }
   
   @Override
   public List<WishListResponseDto> viewWish(String userId) {
       return wishRepository.viewWish(userId).stream()
               .map(WishListResponseDto::new)
               .collect(Collectors.toList());
   }
  ```
- This DTO can be also used as save logic to use builder()
  ```java
  WishListRequestDto requestDto = WishListRequestDto.builder()
		.userId(userId)
		.hostNum(host.getHnum())
		.host(host)
		.build();
  ```


</br>**4-6. Booking (Continue...)**

