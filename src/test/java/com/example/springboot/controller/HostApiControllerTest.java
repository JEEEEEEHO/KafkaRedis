package com.example.springboot.controller;


import com.example.springboot.controller.dto.host.HostListResponseDto;
import com.example.springboot.controller.dto.host.HostUpdateRequestDto;
import com.example.springboot.controller.dto.host.HostsearchReqeustDto;
import com.example.springboot.domain.host.*;
import com.example.springboot.domain.resrv.ResrvDscn;
import com.example.springboot.domain.resrv.ResrvHis;
import com.example.springboot.domain.user.Role;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.service.host.HostService;
import com.example.springboot.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HostApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HostImgRepository hostImgRepository;

    @Autowired
    private HostMainImgRepository hostMainImgRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Autowired
    HostService hostService;


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @Mock
    HttpServletRequest request;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
    @After
    public void tearDown() throws Exception{
        hostImgRepository.deleteAll();
        hostRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Hosts_save() throws Exception{
        // given

        User user = userRepository.save(User.builder()
                .name("Jeeho Kim")
                .email("email")
                .build());

        String region = "1";
        String gender = "1";
        String age = "1";
        String farmsts = "1";
        String shortintro = "shortintro";
        String intro = "intro";
        String lat = "lat";
        String lng ="lng";
        String maxPpl = "1";

        HostSaveRequestDto requestDto = HostSaveRequestDto.builder()
                .user(user)
                .region(region)
                .gender(gender)
                .age(age)
                .farmsts(farmsts)
                .shortintro(shortintro)
                .intro(intro)
                .lat(lat)
                .lng(lng)
                .maxPpl(maxPpl)
                .build();

        MockMultipartFile[] file = new MockMultipartFile[1];

        // when

        // then
        List<Host> all = hostRepository.findAll();
        assertThat(all.get(0).getRegion()).isEqualTo(region);
        assertThat(all.get(0).getIntro()).isEqualTo(intro);

   }

   @DisplayName("PUT/호스트 수정 테스트")
   @Test
    public void updateHostTest() throws ParseException, IOException {
    // given
        // 1) user 정보 저장
       User user = userService.create(User.builder()
                       .name("Jeeho Kim")
                       .email("email")
                       .password("11")
                       .id("233230")
                       .role(Role.USER)
                       .authProvider("Google")
                       .build());
        // 2) host 정보
       String dateStr = "2021년 06월 19일 21시 05분 07초";
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
       Date date = formatter.parse(dateStr);

       HostSaveRequestDto saveRequestDto = HostSaveRequestDto.builder()
               .user(user)
               .region("1")
               .gender("1")
               .age("1")
               .farmsts("1")
               .shortintro("1")
               .intro("1")
               .address("1")
               .lat("1")
               .lng("1")
               .maxPpl("1")
               .apprv_date(date)
               .build();
        // 3) File 정보 - originFileName getInputStream fileUri
       String originalFilename = "originalFilename";
       String contentType = "jpg";
       String filepath = "src/test/resources/testImage/"+originalFilename;

       MockMultipartFile file = new MockMultipartFile("fileName", originalFilename, contentType, filepath.getBytes());
        // 4) 저장
        String hostnum = hostService.save(saveRequestDto, file);
        Host host = hostRepository.findByHnum(Long.valueOf(hostnum));
        // 5) host 수정 정보
       String region = "2";
       String deleteMainImg = "originalFilename";
       HostUpdateRequestDto updateRequestDto = HostUpdateRequestDto.builder() // 프론트엔드에서 기존 값은 value로 해서 가져와야함
               .hostNum(hostnum)
               .region(region)
               .gender(host.getGender())
               .age(host.getAge())
               .farmsts(host.getFarmsts())
               .shortintro(host.getShortintro())
               .intro(host.getIntro())
               .address(host.getAddress())
               .lat(host.getLat())
               .lng(host.getLng())
               .deleteMainImg(deleteMainImg)
               .build();
       // 새로운 파일 내용
       String udtOriginalFilename = "udtOriginalFilename";
       String udtContentType2 = "jpg";
       String udtFilePath = "src/test/resources/testImage/"+udtOriginalFilename;
       MockMultipartFile udtFile = new MockMultipartFile("fileName", udtOriginalFilename, udtContentType2, udtFilePath.getBytes());


    // when - 꺼냄
       String udtHostnum = hostService.update(updateRequestDto, udtFile);
       Host udtHost = hostRepository.findByHnum(Long.valueOf(udtHostnum));
       HostMainImg udtHostMainImg = hostMainImgRepository.findMainImg(Long.valueOf(udtHostnum));

    // then
       // 1) 수정할 떄 반납하는 객체 = 수정해서 keyword로 나온 값이 같은지 확인
       assertThat(udtHost.getRegion()).isEqualTo(region);
       assertThat(udtHostMainImg.getFilename()).isEqualTo(udtOriginalFilename);


   }

    @DisplayName("GET/호스트 리스트 조회 테스트")
    @Test
    public void testViewHostList() throws ParseException, IOException {
        //given
        // 1) user 정보 저장
        User user = userService.create(User.builder()
                .name("Jeeho Kim")
                .email("email")
                .password("11")
                .id("233230")
                .role(Role.USER)
                .authProvider("Google")
                .build());
        // 2) host 정보
        String dateStr = "2021년 06월 19일 21시 05분 07초";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        Date date = formatter.parse(dateStr);

        HostSaveRequestDto saveRequestDto = HostSaveRequestDto.builder()
                .user(user)
                .region("1")
                .gender("1")
                .age("1")
                .farmsts("1")
                .shortintro("1")
                .intro("1")
                .address("1")
                .lat("1")
                .lng("1")
                .maxPpl("1")
                .apprv_date(date)
                .build();
        // 3) File 정보 - originFileName getInputStream fileUri
        String originalFilename = "originalFilename";
        String contentType = "jpg";
        String filepath = "src/test/resources/testImage/"+originalFilename;

        MockMultipartFile file = new MockMultipartFile("fileName", originalFilename, contentType, filepath.getBytes());
        // 4) 저장
        String hostnum = hostService.save(saveRequestDto, file);

        //when
        List<HostListResponseDto> list = hostService.findAllHost();

        //then
        HostListResponseDto hostListResponseDto = list.get(0);
        assertThat(hostListResponseDto.getShortintro()).isEqualTo(saveRequestDto.getShortintro());
        assertThat(hostListResponseDto.getHostMainImg().getFilename()).isEqualTo(originalFilename);


    }

    @DisplayName("GET/호스트 검색 테스트")
    @Test
    public void testHostSearchList() throws IOException, ParseException {
    //given
        // 1) user 정보 저장
        User user = userService.create(User.builder()
                .name("Jeeho Kim")
                .email("email")
                .password("11")
                .id("233230")
                .role(Role.USER)
                .authProvider("Google")
                .build());
        // 2) host 정보
        String dateStr = "2021년 06월 19일 21시 05분 07초";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        Date date = formatter.parse(dateStr);

        HostSaveRequestDto saveRequestDto = HostSaveRequestDto.builder()
                .user(user)
                .region("1")
                .gender("1")
                .age("1")
                .farmsts("1")
                .shortintro("test")
                .intro("1")
                .address("1")
                .lat("1")
                .lng("1")
                .maxPpl("3")
                .apprvYn("Y")
                .apprv_date(date)
                .build();
        // 3) File 정보 - originFileName getInputStream fileUri
        String originalFilename = "originalFilename";
        String contentType = "jpg";
        String filepath = "src/test/resources/testImage/"+originalFilename;

        MockMultipartFile file = new MockMultipartFile("fileName", originalFilename, contentType, filepath.getBytes());
        // 4) 저장
        String hostnum = hostService.save(saveRequestDto, file);

        // -> Host 가 저장됨

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMDD");

        // 5) 예약 테이블 저장하기
        ResrvHis resrvHis = ResrvHis.builder()
                .resrvNum(1L)
                .host(hostRepository.findByHnum(Long.valueOf(hostnum)))
                .reqPpl("2")
                .userid("email")
                .startDate(simpleDateFormat.parse("20230702"))
                .endDate(simpleDateFormat.parse("20230709"))
                .accptYn("Y")
                .build();

        // 6) 예약 확정 테이블
        ResrvDscn resrvDscn = ResrvDscn.builder()
                .resrvNum(resrvHis.getResrvNum())
                .resrvHis(resrvHis)
                .acptdDate(simpleDateFormat.parse("20230605"))
                .restPpl(1)
                .build();

    // when
        // 1) Host 조건을 만족 X ->
//        HostsearchReqeustDto hostsearchReqeustDto1 = HostsearchReqeustDto.builder()
//                .region("2") // 여기서 만족하지 않음
//                .gender("1")
//                .farmsts("1")
//                .people("1")
//                .startDate("20230801")
//                .endDate("20230810")
//                .build();

        // 2) HostResrv 테이블에 조건 만족 X
//        HostsearchReqeustDto hostsearchReqeustDto2 = HostsearchReqeustDto.builder()
//                .region("1")
//                .gender("1")
//                .farmsts("1")
//                .people("2") // 여기서 만족하지 않음
//                .startDate("20230701") // 여기서 만족하지 않음
//                .endDate("20230705")
//                .build();


        // 3) 조건 만족
        HostsearchReqeustDto hostsearchReqeustDto3 = HostsearchReqeustDto.builder()
                .region("1")
                .gender("1")
                .farmsts("1")
                .people("1")
                .startDate("20230801")
                .endDate("20230810")
                .build();

    // then
        List<HostListResponseDto> list = hostService.searchHost(hostsearchReqeustDto3);

        HostListResponseDto hostListResponseDto = list.get(0);
        assertThat(hostListResponseDto.getShortintro()).isEqualTo(saveRequestDto.getShortintro());

    }
}
