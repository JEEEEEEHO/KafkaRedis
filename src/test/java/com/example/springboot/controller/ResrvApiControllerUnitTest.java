package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.controller.dto.reserv.ResrvHistRequestDto;
import com.example.springboot.domain.cpn.Cpn;
import com.example.springboot.domain.cpn.CpnRepository;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.service.host.HostService;
import com.example.springboot.service.resrv.ResrvService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ResrvApiControllerUnitTest {
    @InjectMocks
    private ResrvService resrvService;
    @Mock
    private CpnRepository cpnRepository;
    @Mock
    private HostRepository hostRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    public void setup() throws ParseException, IOException {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("동시성 단위 테스트")
    @Test
    public void IVTCNT_CONCURRENT_SERVICE_UNIT_TEST() throws InterruptedException, ParseException {
        // given
        // 날짜 포맷 설정
        String dateStr = "2021년 06월 19일 21시 05분 07초";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        Date date = formatter.parse(dateStr);

        // 호스트 저장 Mock
        User user = User.builder()
                .id("host123") // 요청할 때마다 새로운 user 가 저장되게끔 해봄
                .name("HostKim")
                .email("HostEmail")
                .build();

        when(hostRepository.save(any(Host.class))).thenReturn(
                Host.builder()
                        .hnum(1L)
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
                        .build()
        );

        // 쿠폰 저장 Mock
        when(cpnRepository.save(any(Cpn.class))).thenReturn(
                Cpn.builder()
                        .cpnNum(1L)
                        .maxCnt(10)
                        .regDt(date)
                        .regId("jeeho")
                        .ivtCnt(10)
                        .build()
        );
        // Redis Mock
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // 사용자 Mock 설정
        UUID uuid = UUID.randomUUID();
        User user1= User.builder()
                .id(String.valueOf(uuid)) // 요청할 때마다 새로운 user 가 저장되게끔 해봄
                .name("Jeeho Kim")
                .email("email")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user1);

        // 예약 요청 DTO 생성
        ResrvHistRequestDto histRequestDto = ResrvHistRequestDto.builder()
                .hnum(String.valueOf(1L))
                .userid(user.getId())
                .reqPpl(2)
                .build();

        // Principal mock 객체 생성
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn(user.getId());


        // 스레드 생성
        final int numberOfOrders = 7; // 동시 요청 스레드
        ExecutorService es = Executors.newFixedThreadPool(5); // 스레드 풀 크기 고정
        CountDownLatch latch = new CountDownLatch(numberOfOrders);

        for (int i = 0; i < numberOfOrders; i++) {
            es.execute(() -> {
                try {
                    resrvService.saveRequest(mockPrincipal, histRequestDto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        es.shutdown();

        // then
        verify(hostRepository.countIvtPpl(1L), times(numberOfOrders));
    }
}
