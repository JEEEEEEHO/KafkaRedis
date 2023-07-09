package com.example.springboot.service.post;

import com.example.springboot.domain.post.Post;
import com.example.springboot.domain.post.PostRepository;
import com.example.springboot.controller.dto.post.PostListResponseDto;
import com.example.springboot.controller.dto.post.PostResponseDto;
import com.example.springboot.controller.dto.post.PostSaveRequestDto;
import com.example.springboot.controller.dto.post.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    //전체 조회
    @Transactional
    public List<PostListResponseDto> findAllDesc(){
        return postRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 상세보기
    public PostResponseDto findById(Long pnum) {
        Post entity = postRepository.findById(pnum)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다 id = "+pnum));
        return new PostResponseDto(entity);
    }

    // 저장
    @Transactional//여러기능을 하나로 묶어 실행해서 하나라도 잘못되면 모두 취소해야한다(데이터 무결성 보장).
    public long save(PostSaveRequestDto requestDto){
        return postRepository.save(requestDto.toEntity()).getPnum();
    }


    // 수정
    @Transactional
    public Long update(Long pnum, PostUpdateRequestDto requestDto) {
        Post posts = postRepository.findById(pnum).orElseThrow(()->
                new IllegalArgumentException("해당 게시글이 없습니다. pnum = "+pnum));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return pnum;
    }

    // 삭제
    @Transactional
    public void delete(Long pnum) {
        Post posts = postRepository.findById(pnum)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다 id = "+pnum)
                        );
        postRepository.delete(posts);
    }


}
