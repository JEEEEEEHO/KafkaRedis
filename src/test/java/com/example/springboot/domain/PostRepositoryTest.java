package com.example.springboot.domain;

import com.example.springboot.domain.post.Post;
import com.example.springboot.domain.post.PostRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @After
    // 배포 전 전체 테스트를 수행할 때 테스트간 데이터 침범을 막기 위해 사용
    public void cleanup(){
        postRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기(){
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postRepository.save(Post.builder()
                // 테이블 posts에 insert/update 쿼리를 실행
                // id 값이 있으면 update, 없으면 insert
                .title(title)
                .content(content)
                .author("jeeho")
                .build());
        //when
        List<Post> postsList = postRepository.findAll();

        //then
        Post posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_등록(){
        //given 초기화
        LocalDateTime now = LocalDateTime.of(2019,6,4,0,0,0);
        postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        //when
        List<Post> postsList = postRepository.findAll();
        //then
        Post posts = postsList.get(0);

        System.out.println(">>>>>>>>>> createDate = "+posts.getCreatedDate()+", modifiedDate = "+posts.getModifiedDate());
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}
