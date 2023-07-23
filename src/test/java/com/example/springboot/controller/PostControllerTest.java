package com.example.springboot.controller;

import com.example.springboot.config.auth.SecurityConfig;
import com.example.springboot.domain.post.Post;
import com.example.springboot.domain.post.PostRepository;
import com.example.springboot.service.post.PostService;
import com.example.springboot.service.post.PostServiceImpl;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PostController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
public class PostControllerTest {


    @MockBean
    private PostRepository postRepository;

    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mvc;


    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception { // 테스트가 끝날 때 마다
        postRepository.deleteAll();
    }



    @WithMockUser(roles = "USER")
    @Test
    public void postFormPageLoading() throws Exception {
        //given
        String test= "등록";
        //when

        mvc.perform(get("/post/form"))
                .andExpect(status().isOk());

        //then

    }

    @WithMockUser(roles = "USER")
    @Test
    public void postDetail() throws Exception {
        //given
        String title = "title";
        String content = "content";
        postRepository.save(Post.builder()
                .title(title)
                .content(content)
                .author("author")
                .build());


        //when
        List<Post> postsList = postRepository.findAll();
        long postNum = postsList.get(0).getPnum();

        //then
        mvc.perform(get("/post/"+postNum)).andExpect(status().isOk());
    }
}