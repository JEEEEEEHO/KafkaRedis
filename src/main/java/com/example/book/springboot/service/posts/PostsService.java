package com.example.book.springboot.service.posts;

import com.example.book.springboot.domain.posts.Posts;
import com.example.book.springboot.domain.posts.PostsRepository;
import com.example.book.springboot.web.dto.PostsListResponseDto;
import com.example.book.springboot.web.dto.PostsResponseDto;
import com.example.book.springboot.web.dto.PostsSaveRequestDto;
import com.example.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    //등록
    private final PostsRepository postsRepository;
    @Transactional//여러기능을 하나로 묶어 실행해서 하나라도 잘못되면 모두 취소해야한다(데이터 무결성 보장).
    public long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    // 수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("해당 게시글이 없습니다. id = "+id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    //전체 조회
    @Transactional
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }


    // 상세
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다 id = "+id));
        return new PostsResponseDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다 id = "+id)
                        );
        postsRepository.delete(posts);
    }
}
