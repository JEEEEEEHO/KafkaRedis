package com.example.springboot.controller;

import com.example.springboot.controller.dto.post.PostUpdateRequestDto;
import com.example.springboot.domain.post.Post;
import com.example.springboot.domain.post.PostRepository;
import com.example.springboot.service.post.PostService;
import org.apache.catalina.security.SecurityConfig;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

// For mockMvc

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PostApiController.class,
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
public class PostApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

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

    @Test
    @WithMockUser(roles = "USER")
    public void register() throws Exception {
        //given
        String title = "title";
        String content = "content";
        postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        //when
        List<Post> postsList = postRepository.findAll();

        //then
        Post post = postsList.get(0);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getTitle()).isEqualTo(content);

    }
    @Test
    @WithMockUser(roles = "USER")
    public void update() throws Exception {
        //given
        Post savedPosts = postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updatePnum = savedPosts.getPnum();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        //when
        postService.update(updatePnum, requestDto);

        //then
        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}