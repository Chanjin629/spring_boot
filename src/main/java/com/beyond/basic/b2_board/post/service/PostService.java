package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.dto.PostSearchDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // claims 의 subject : email
//        authorId가 실제 있는지 확인필요
//        Author author = authorRepository.findById(dto.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("없는사용자 입니다"));
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는사용자 입니다"));
        LocalDateTime appointmentTime = null;
        if(dto.getAppointment().equals("Y")){
            if(dto.getAppointmentTime() == null || dto.getAppointmentTime().isEmpty()){
                throw new IllegalArgumentException("시간정보가 비어져 있습니다");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(dto.getAppointmentTime(), dateTimeFormatter);
        }
        postRepository.save(dto.toEntity(author, appointmentTime));
    }

    public PostDetailDto findById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("없는 ID 입니다"));
//        엔티티간의 관계성 설정을 하지 않았을때
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(()->new EntityNotFoundException("없는 작가ID 입니다"));
//        PostDetailDto dto = PostDetailDto.fromEntity(post, author);

//        엔티티간의 관계성 설정을 통해 Author 객체를 쉽게 조회하는경우
        return PostDetailDto.fromEntity(post);
    }

//    public List<PostListDto> findAll(){
//        List<Post> postList = postRepository.findAll();
//        return postList.stream().map(a->PostListDto.fromEntity(a)).collect(Collectors.toList());
//    }
    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto dto){
//        List<Post> postList = postRepository.findAll();
//        List<Post> postList = postRepository.findAllJoin();
//        List<Post> postList = postRepository.findAllFetchJoin();
//        postlist 조회할때 참조관계에 있는 author까지 조회하게 되므로, N(author쿼리) + 1(post쿼리) 문제발생
//        jpa는 기본방향성이 fetch lazy이므로, 참조하는 시점에 쿼리를내보내게 되어 JOIN문을 만들어주지 않고, N+1 문제발생

//        검색을 위해 Specification 객체 스프링에서 제공
//        Specification 객체는 복잡한 쿼리를 명세를 이용하여 정의하는 방식으로, 쿼리를 쉽게 생성
//        페이지처리 findAll 호출
//        stream() 포함하고 있음
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                Root : 엔티티의 속성을 접근하기 위한 객체, CriteriaBuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("delYn"),"N"));
                predicateList.add(criteriaBuilder.equal(root.get("appointment"),"N"));
                if(dto.getCategory() != null){
                    predicateList.add(criteriaBuilder.equal(root.get("category"),dto.getCategory()));
                }
                if(dto.getTitle() != null){
                    predicateList.add(criteriaBuilder.like(root.get("title"),"%"+dto.getTitle()+"%"));
                }
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for (int i = 0; i < predicateList.size(); i++) {
                    predicateArr[i] = predicateList.get(i);
                }
//                위에 검색조건들을 하나(한줄) 의 Predicate 객체롤 만들어서 return
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };
        Page<Post> postList = postRepository.findAll(specification,pageable);
//        return postList.stream().map(a->PostListDto.fromEntity(a)).collect(Collectors.toList());
        return postList.map(a->PostListDto.fromEntity(a));
    }




}
