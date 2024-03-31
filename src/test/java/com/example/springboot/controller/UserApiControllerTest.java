package com.example.springboot.controller;

import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.service.user.UserService;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserApiControllerTest extends TestCase {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;
//    @Before
//    public void setup() {
//        mvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(SecurityMockMvcConfigurers.springSecurity())
//                .build();
//    }
    @After
    public void tearDown() throws Exception { // 테스트가 끝날 때 마다
        userRepository.deleteAll();
    }


    @Test
    public void signup() throws Exception{
        // given
        String name = "Kim";
        String email = "test";
        String password = "1234";

        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
        // when
        User registeredUser = userService.create(user);

        //then
        assertThat(registeredUser.getName()).isEqualTo(name);
    }


}