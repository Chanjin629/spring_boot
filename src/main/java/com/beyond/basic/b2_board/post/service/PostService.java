package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
//@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto dto){
//        authorId가 실제 있는지 확인필요
        Author author = authorRepository.findById(dto.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("없는사용자 입니다"));
        postRepository.save(dto.toEntity(author));
    }

    public PostDetailDto findById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("없는 ID 입니다"));
//        엔티티간의 관계성 설정을 하지 않았을때
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(()->new EntityNotFoundException("없는 작가ID 입니다"));
//        PostDetailDto dto = PostDetailDto.fromEntity(post, author);

//        엔티티간의 관계성 설정을 통해 Author 객체를 쉽게 조회하는경우
        return PostDetailDto.fromEntity(post);
    }

    public List<PostListDto> findAll(){
        List<Post> postList = postRepository.findAll();
        return postList.stream().map(a->PostListDto.fromEntity(a)).collect(Collectors.toList());
    }
}
