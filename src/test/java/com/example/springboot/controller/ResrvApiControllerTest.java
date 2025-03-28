package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.controller.dto.reserv.ResrvHistRequestDto;
import com.example.springboot.domain.cpn.Cpn;
import com.example.springboot.domain.cpn.CpnRepository;
import com.example.springboot.domain.host.HostImgRepository;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.resrv.ResrvDscnRepository;
import com.example.springboot.domain.resrv.ResrvHis;
import com.example.springboot.domain.resrv.ResrvHisRepository;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.service.host.HostService;
import com.example.springboot.service.resrv.ResrvService;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ResrvApiControllerTest extends TestCase {

    @Autowired
    ResrvService resrvService; // 서비스 로직 테스트용

    @Autowired
    HostService hostService; // 굳이 없어도 되긴함

    @Autowired
    CpnRepository cpnRepository;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HostImgRepository hostImgRepository;

    @Autowired
    ResrvDscnRepository resrvDscnRepository;

    @Autowired
    ResrvHisRepository resrvHisRepository;

    @Autowired
    private MockMvc mockMvc; // mvc를 mocking으로 테스트

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    HttpServletRequest request;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
    @After
    public void tearDown() throws Exception {
        hostImgRepository.deleteAll();
        hostRepository.deleteAll();
        userRepository.deleteAll();
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }


    @DisplayName("동시성 통합테스트")
    @Test
    public void IVTCNT_CONCURRENT_ASIS_TEST() throws ParseException, IOException, InterruptedException {
        // given
        Set<String> set = new HashSet<>();
        // 사용자 저장
        User user = userRepository.save(User.builder()
                .id("testUser")
                .name("Jeeho Kim")
                .email("email")
                .build());

        // 호스트 저장
        String dateStr = "2021년 06월 19일 21시 05분 07초";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        Date date = formatter.parse(dateStr);
        String originalFilename = "originalFilename";
        String contentType = "jpg";
        String filepath = "src/test/resources/testImage/"+originalFilename;
        MockMultipartFile file = new MockMultipartFile("fileName", originalFilename, contentType, filepath.getBytes());


        HostSaveRequestDto saveRequestDto = HostSaveRequestDto.builder()
                .user(user)
                .region("1")
                .gender("1")
                .age("1")
                .farmsts("1")
                .shortintro("1")
                .intro("1")
                .address("1")
                .maxPpl(10)
                .ivtPpl(10)
                .apprv_date(date)
                .build();
        String hostnum = hostService.save(saveRequestDto, file);

        // 쿠폰 저장
        cpnRepository.save(Cpn.builder()
                                .cpnNum(1L)
                                .maxCnt(10)
                                .regDt(date)
                                .regId("jeeho")
                                .ivtCnt(10) // 최초 재고
                                .build()
                            );

        // 요청 DTO
        ResrvHistRequestDto histRequestDto = ResrvHistRequestDto.builder()
                .hnum(hostnum)
                .userid(user.getId())
                .reqPpl(2)
                .build();

        // Principal mock 객체
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn(user.getId());

        // when
        // 스레드 생성
        final int numberOfOrders = 7; // 스레드 7개 요청됨
        ExecutorService es = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(numberOfOrders); // 모든 스레드가 완료될 때까지 대기

        for (int i = 0; i < numberOfOrders; i++) {
            String taskName = "thread" + i;
            es.execute(() -> {
                try {
                    System.out.println("현재 스레드정보 :[" + taskName+"]");
                    resrvService.saveRequest(mockPrincipal, histRequestDto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown(); // 주문 처리 후 countDown
                }
            });
        }
        latch.await(); // 모든 스레드가 끝날 때까지 대기
        es.shutdown(); // 스레드 풀 종료

        // then
        // 2명 인원 요청 7개가 동시에 들어왔다면, 재고는 0이
        assertThat(hostRepository.countIvtPpl(Long.valueOf(hostnum))).isEqualTo(0);

    }


    // 외부 연동 api test code
    @DisplayName("apiUserCreateTest")
    @Test
    public void createUserTest() throws Exception {
        // given
        //요청 생성
        User user = userRepository.save(User.builder()
                        .id("id")
                .name("Jeeho Kim")
                .email("email")
                .build());
        // Resrv_hist 저장
        ResrvHistRequestDto histRequestDto = ResrvHistRequestDto.builder()
                .hnum("1")
                .userid(user.getId())
                .reqPpl(2)
                .build();

        // 1️⃣ Mock Principal 객체 생성

        Authentication authentication = mock(Authentication.class);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext); // SecurityContext에 인증 정보 설정
        String json = new ObjectMapper().writeValueAsString(histRequestDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("http://localhost:8080/api/resrv/save")
                        .content(json) // JSON 데이터 전송
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(
                        MockMvcResultMatchers.status().isOk()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.hnum").value("1")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.response.resultCode").value("OK")
                )
                .andDo(MockMvcResultHandlers.print());
    }


    @DisplayName("GET/호스트별 예약 요청 저장")
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
                .maxPpl(4)
                .apprv_date(date)
                .build();

        // 3) File 정보 - originFileName getInputStream fileUri
        String originalFilename = "originalFilename";
        String contentType = "jpg";
        String filepath = "src/test/resources/testImage/"+originalFilename;

        MockMultipartFile file = new MockMultipartFile("fileName", originalFilename, contentType, filepath.getBytes());

        // 4) 저장
        String hostnum = hostService.save(saveRequestDto, file);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMDD");

        // when
        // Resrv_hist 저장
        ResrvHistRequestDto histRequestDto = ResrvHistRequestDto.builder()
                .hnum(hostnum)
                .userid(user.getId())
                .startDate(simpleDateFormat.parse("20230702"))
                .endDate(simpleDateFormat.parse("20230709"))
                .reqPpl(2)
                .build();

        ResrvHis resrvHis = resrvHisRepository.save(histRequestDto.toEntity());

        // then
        assertThat(String.valueOf(resrvHis.getHnum())).isEqualTo(hostnum);
    }

    @DisplayName("GET/호스트별 예약 확정날짜 확인")
    @Test
    public void ResrvDscn_HostDetail_find() throws Exception {
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
                .maxPpl(4)
                .apprv_date(date)
                .build();

        // 3) File 정보 - originFileName getInputStream fileUri
        String originalFilename = "originalFilename";
        String contentType = "jpg";
        String filepath = "src/test/resources/testImage/"+originalFilename;

        MockMultipartFile file = new MockMultipartFile("fileName", originalFilename, contentType, filepath.getBytes());

        // 4) 저장
        String hostnum = hostService.save(saveRequestDto, file);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMDD");


        // Resrv_hist 저장
        ResrvHistRequestDto histRequestDto = ResrvHistRequestDto.builder()
                .hnum(hostnum)
                .userid(user.getId())
                .startDate(simpleDateFormat.parse("20230702"))
                .endDate(simpleDateFormat.parse("20230709"))
                .reqPpl(2)
                .build();

        ResrvHis resrvHis = resrvHisRepository.save(histRequestDto.toEntity());

        // when
        List<ResrvDscnResponseDto> responseDtoList = resrvService.findResrvDscnByHost(hostnum);

        // then
        assertThat(String.valueOf(resrvHis.getHnum())).isEqualTo(hostnum);
    }
}