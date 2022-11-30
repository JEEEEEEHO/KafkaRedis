package com.example.book.springboot.domain.posts;

import com.example.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Posts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // pk 생성규칙, 자동으로 1씩 증가
    private Long id;

    @Column(length = 500, nullable = false)
    // 굳이 선언하지 않아도 해당 클래스의 필드는 모두 테이블의 컬럼이 됨
    // 추가로 변경이 필요한 옵션이 있을 때 사용
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    // 빌더 패턴, 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함
    // setter 없이 builder 생성자를 통해 값을 채운 후 삽입
    // 어느 필드에 어떤 값이 들어갈지가 명확해짐
    public Posts(String title, String content, String author){
        this.title=title;
        this.content=content;
        this.author=author;
    }

    public void update(String title, String content){
        this.title=title;
        this.content=content;
    }
}
