package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
//import com.beyond.basic.b2_board.repository.AuthorJdbcRepository;
//import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// Component 로도 대채 가능(트랜잭션처리가 없는 경우에)
@Service
@RequiredArgsConstructor
// 스프링에서 메서드단위로 트랜잭션처리(commit)를 하고, 만약 예외(unchecked)발생시 자동 롤백처리 지원
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
    private final PasswordEncoder passwordEncoder;
    public void save(AuthorCreateDto authorCreateDto){
        // 이메을 중복검증
        if(authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }

        // toEntity 패턴을 통해 Author 객체 조립을 공통화
        String encodedPassword = passwordEncoder.encode(authorCreateDto.getPassword());
        Author author = authorCreateDto.authorToEntity(encodedPassword);//new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
//        cascading 테스트 : 회원이 생성될때, 곹바로 "가입인사" 글을 생성하는 상황
//        방법 2가지
//        방법1. 직접 POST객체 생성 후 저장
        Post post = Post.builder()
                .title("안녕하세요")
                .contents(authorCreateDto.getName() + "입니다. 반갑습니다")
//                author 객체가 db에 save 되는 순간 엔티티매니저와 영속성컨텍스트에 의해 author 객체에도 id값 생성
                .author(author)
                .build();
//        postRepository.save(post);
//        방법2. cascade옵션 활용
        author.getPostList().add(post);
        this.authorRepository.save(author);
    }

    public Author doLogin(AuthorLoginDto dto){
        Optional<Author> optionalAuthor = authorRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if(!optionalAuthor.isPresent()){
            check = false;
        } else {
            // 비밀번호 일치여부 검증 : matches 함수를 통해서 암호되지 않은값을 다시 암호화하여 db의 password 검증
            if(!passwordEncoder.matches(dto.getPassword(), optionalAuthor.get().getPassword())){
                check = false;
            }
        }
        if(!check){
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다");
        }
        return optionalAuthor.get();

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
