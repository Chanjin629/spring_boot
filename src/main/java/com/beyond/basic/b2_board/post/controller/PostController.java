package com.beyond.basic.b2_board.post.controller;

import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.author.dto.CommonDto;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import com.beyond.basic.b2_board.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private  final PostService postService;
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PostCreateDto dto) {
        postService.save(dto);
        return new  ResponseEntity<>(new CommonDto("ok", HttpStatus.CREATED.value(),"Post is created" ), HttpStatus.CREATED);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        PostDetailDto dto = postService.findById(id);
        return new ResponseEntity<>(new CommonDto(dto, HttpStatus.OK.value(),"post is found"), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(){
        List<PostListDto> postListDtos = postService.findAll();
        return new ResponseEntity<>(new CommonDto(postListDtos, HttpStatus.OK.value(),"post is found"), HttpStatus.OK);
    }
}
