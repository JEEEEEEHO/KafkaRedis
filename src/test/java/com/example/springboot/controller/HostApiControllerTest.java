package com.example.springboot.controller;


import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.user.Role;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HostApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception{
        hostRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Hosts_등록() throws Exception{
        // given
        int region = 1;
        int gender = 1;
        int age = 1;
        int farmsts = 2;
        String shortintro = "shortintro";
        String intro = "intro";
        String lat = "lat";
        String lng ="lng";

        Role role = Role.GUEST;

        User user = userRepository.save( User.builder()
                .name("Jeeho Kim")
                .email("email")
                .build());

        String userid = user.getId();

        HostSaveRequestDto requestDto = HostSaveRequestDto.builder()
                .id(userid)
                .region(region)
                .gender(gender)
                .age(age)
                .farmsts(farmsts)
                .shortintro(shortintro)
                .intro(intro)
                .lat(lat)
                .lng(lng)
                .build();
        String url = "http://localhost:" + port + "/api/v1/hosts";

        // when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
            .andExpect(status().isOk());

        // then
        List<Host> all = hostRepository.findAll();
        assertThat(all.get(0).getRegion()).isEqualTo(region);
        assertThat(all.get(0).getIntro()).isEqualTo(intro);

    }
}
