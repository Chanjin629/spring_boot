package com.beyond.basic.b2_board.service;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// Component 로도 대채 가능(트랜잭션처리가 없는 경우에)
@Service
//@Transactional
@RequiredArgsConstructor
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
    private final AuthorMemoryRepository authorMemoryRepository;
    public void save(AuthorCreateDto authorCreateDto){
        // 이메을 중복검증
        if(authorMemoryRepository.findByEmail(authorCreateDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }
        // toEntity 패턴을 통해 Author 객체 조립을 공통화
        Author author = authorCreateDto.authorToEntity();//new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        this.authorMemoryRepository.save(author);

    }
    public List<AuthorListDto> findAll(){
        return authorMemoryRepository.findAll().stream()
                .map(a->a.listFromEntity()).collect(Collectors.toList());
//        List<AuthorListDto> dtoList = new ArrayList<>();
//        for(Author a : authorMemoryRepository.findAll()){
//            AuthorListDto dto = a.listFromEntity(); //new AuthorListDto(a.getId(),a.getName(),a.getEmail());
//            dtoList.add(dto);
//        }
//        return dtoList;
    }

    public AuthorDetailDto findById(Long id){
        Author author  = authorMemoryRepository.findById(id).orElseThrow(()->new NoSuchElementException("없는아이디"));
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author); //author.detailFromEntity();//new AuthorDetailDto(author.getId(),author.getName(),author.getEmail());
        return dto;
    }

    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto){
        Author author = authorMemoryRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(()->new NoSuchElementException("no email found"));
        author.updatePw(authorUpdatePwDto.getPassword());
    }
    public void delete(Long id){
        authorMemoryRepository.findById(id).orElseThrow(()->new NoSuchElementException("없는 사용자입니다"));
        authorMemoryRepository.delete(id);
    }
}
