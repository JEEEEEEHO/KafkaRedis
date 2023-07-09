package com.example.springboot.controller;

import com.example.springboot.config.auth.SecurityConfig;
import com.example.springboot.service.post.PostServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import  static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@WebMvcTest(controllers = IndexApiController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
public class IndexApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostServiceImpl postServiceImpl;

    @WithMockUser(roles = "USER")
    @Test
    public void 메인페이지_로딩() throws Exception {
        //THEN
        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }


    @WithMockUser(roles = "USER")
    @Test
    public void 마이페이지_로딩() throws Exception {
    //Given
        String body = "프로필정보";
    //THEN
        mvc.perform(get("/mypage"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void 호스트등록페이지_로딩() throws Exception {
        //THEN
        mvc.perform(get("/hosts/save"))
                .andExpect(status().isOk());
    }

}
