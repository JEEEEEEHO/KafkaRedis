package com.example.springboot.controller;


import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostImgRepository;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.service.host.HostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.Multipart;
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

        //hostService.save(requestDto);

//        String url = "http://localhost:" + port + "/api/host/save";
//
//
//
//        mvc.perform(post(url)
//                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                .content(objectMapper.writeValueAsString(requestDto)))
//            .andExpect(status().isOk());

        // then
        List<Host> all = hostRepository.findAll();
        assertThat(all.get(0).getRegion()).isEqualTo(region);
        assertThat(all.get(0).getIntro()).isEqualTo(intro);

   }
}
