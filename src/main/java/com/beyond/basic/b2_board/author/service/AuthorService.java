package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.author.dto.AuthorUpdatePwDto;
//import com.beyond.basic.b2_board.repository.AuthorJdbcRepository;
//import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// Component 로도 대채 가능(트랜잭션처리가 없는 경우에)
@Service
@RequiredArgsConstructor
// 스프링에서 메서드단위로 트랜잭션처리를 하고, 만약 예외(unchecked)발생시 자동 롤백처리 지원
@Transactional
public class AuthorService {

//    // 의존성주입(DI)방법1. Autowired 어노테이션 사용 -> 필드주입
//    @Autowired
//    private AuthorRepository authorRepository;

    // 의존성주입(DI)방법2. 생성자주입방식(가장많이 쓰는방식)
    // 장점 1) final을 통해 상수로 사용가능(안정성향상) 2) 다형성 구현가능 3) 순환참조방지(컴파일타입에 check)
//    private final AuthorRepositoryInterface authorRepository;
//    // 객체로 만들어지는 시점에 스프링에서 authorRepository 객체를 매개변수로 주입
//    @Autowired // 생성자가 하나밖에 없을때에는 Autowired 생략 가능
//    public AuthorService(AuthorMemoryRepository authorRepository) {
//        this.authorRepository = authorRepository;
//    }

    // 의존성주입방법3. RequiredArgs 어노테이션 사용 -> 반드시 초기화 되어야 하는 필드(final 등)을 대상으로 생성자를 자동생성
    // 다형성 설계는 불가
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    public void save(AuthorCreateDto authorCreateDto){
        // 이메을 중복검증
        if(authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }
        // toEntity 패턴을 통해 Author 객체 조립을 공통화
        Author author = authorCreateDto.authorToEntity();//new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        this.authorRepository.save(author);

    }
    // 트랜잭션이 필요없는경우, 아래와같이 명시적으로 제외
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll(){
        return authorRepository.findAll().stream()
                .map(a->a.listFromEntity()).collect(Collectors.toList());
//        List<AuthorListDto> dtoList = new ArrayList<>();
//        for(Author a : authorMemoryRepository.findAll()){
//            AuthorListDto dto = a.listFromEntity(); //new AuthorListDto(a.getId(),a.getName(),a.getEmail());
//            dtoList.add(dto);
//        }
//        return dtoList;
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id){
        Author author  = authorRepository.findById(id).orElseThrow(()->new NoSuchElementException("없는아이디"));

//        연관관계 설정없이 직접 조회해서 count 값 찾는경우
//        List<Post> postList = postRepository.findByAuthorId(id);
//        List<Post> postList = postRepository.findByAuthor(author);
//        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author, postList.size());

        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        // author.detailFromEntity();
        // new AuthorDetailDto(author.getId(),author.getName(),author.getEmail());
        return dto;
    }

    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto){
        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(()->new NoSuchElementException("no email found"));
//        dirty checking : 객체를 수정한 후 별도의 update쿼리 발생시키지 않아도, 영속성 컨텍스트에 의해 객체 변경사항 자동 db 반영
        author.updatePw(authorUpdatePwDto.getPassword());
    }
    public void delete(Long id){
        Author author =  authorRepository.findById(id).orElseThrow(()->new NoSuchElementException("없는 사용자입니다"));
        authorRepository.delete(author);
    }
}
