package com.example.springboot.controller;


import com.example.springboot.controller.dto.host.HostUpdateRequestDto;
import com.example.springboot.domain.host.*;
import com.example.springboot.domain.user.Role;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.service.host.HostService;
import com.example.springboot.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.Multipart;
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
    UserService userService;

    @Autowired
    HostService hostService;


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

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
//       FileInputStream fileInputStream = new FileInputStream(filepath); //파일경로로 생성한 InputStream

       MockMultipartFile file = new MockMultipartFile("fileName", originalFilename, contentType, filepath.getBytes());
        // 4) 저장
        String hostnum = hostService.save(saveRequestDto, file);

        // 5) host 수정 정보
       String region = "2";
       String hostDeleteMainImg = "originalFilename";
       HostUpdateRequestDto updateRequestDto = HostUpdateRequestDto.builder()
               .hostNum(hostnum)
               .region(region)
               .hostDeleteMainImg(hostDeleteMainImg)
               .build();
       // 새로운 파일 내용
       String udtOriginalFilename = "udtOriginalFilename";
       String udtContentType2 = "jpg";
       String udtFilePath = "src/test/resources/testImage/"+udtOriginalFilename;
//       FileInputStream udtFileInputStream = new FileInputStream(String.valueOf(udtFilePath)); //파일경로로 생성한 InputStream
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
}
