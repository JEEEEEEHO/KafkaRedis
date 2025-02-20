package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.domain.wish.WishList;
import com.example.springboot.domain.wish.WishRepository;
import com.example.springboot.service.host.HostService;
import com.example.springboot.service.user.UserService;
import com.example.springboot.service.wish.WishService;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WishListApiControllerTest extends TestCase {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    HostService hostService;

    @Autowired
    WishService wishService;

    @Autowired
    WishRepository wishRepository;

    @Mock
    HttpServletRequest request;




    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @DisplayName("POST/위시리트스 저장테스트")
    @Test
    @WithMockUser(roles = "USER")
    public void testSaveWishList() throws IOException, ParseException {
        // given
        User user = userRepository.save(User.builder()
                .name("Jeeho Kim")
                .email("email")
                .build());

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
                .maxPpl("1")
                .apprv_date(date)
                .build();

        String originalFilename = "originalFilename";
        String contentType = "jpg";
        String filepath = "src/test/resources/testImage/"+originalFilename;

        MockMultipartFile file = new MockMultipartFile("fileName", originalFilename, contentType, filepath.getBytes());
        String hostnum = hostService.save(saveRequestDto, file);

        // when
        wishService.saveWish(user.getId(),hostnum);

        // then
        List<WishList> all = wishRepository.findAll();
        assertThat(all.get(0).getHostNum()).isEqualTo(Long.valueOf(hostnum));

    }

    @DisplayName("DELETE/위시리트스 삭제테스트")
    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteWishList() {
    }

    @DisplayName("GET/위시리트스 조회테스트")
    @Test
    @WithMockUser(roles = "USER")
    public void testViewWishList() {
    }
}