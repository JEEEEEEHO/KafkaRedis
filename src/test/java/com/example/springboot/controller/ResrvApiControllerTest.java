package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.domain.host.HostImgRepository;
import com.example.springboot.domain.host.HostMainImgRepository;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.resrv.ResrvDscnRepository;
import com.example.springboot.domain.resrv.ResrvHisRepository;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.service.host.HostService;
import com.example.springboot.service.resrv.ResrvService;
import com.example.springboot.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.apache.catalina.security.SecurityConfig;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ResrvApiController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
public class ResrvApiControllerTest extends TestCase {
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
    ResrvService resrvService;

    @Autowired
    ResrvDscnRepository resrvDscnRepository;

    @Autowired
    ResrvHisRepository resrvHisRepository;


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
    HttpServletRequest request;

    @After
    public void tearDown() throws Exception {
        hostImgRepository.deleteAll();
        hostRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("GET/호스트별 예약확정 목록 찾기")
    @Test
    public void Resrv_HostDetail_find() throws Exception {
        // given
        // User 등록
        User user = userRepository.save(User.builder()
                .name("Jeeho Kim")
                .email("email")
                .build());

        // Host 등록
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
                .maxPpl("4")
                .apprv_date(date)
                .build();

        // 3) File 정보 - originFileName getInputStream fileUri
        String originalFilename = "originalFilename";
        String contentType = "jpg";
        String filepath = "src/test/resources/testImage/"+originalFilename;

        MockMultipartFile file = new MockMultipartFile("fileName", originalFilename, contentType, filepath.getBytes());

        // 4) 저장
        String hostnum = hostService.save(saveRequestDto, file);


        // Resrv_hist 저장



        resrvService.findResrvDscnByHost(hostnum);



    }
}