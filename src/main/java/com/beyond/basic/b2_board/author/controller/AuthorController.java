package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.author.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.author.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // controller + responsebody
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
    // 회원가입
    @PostMapping("/create")
    // ResponseEntity<?> 모든객체 허용가능
    // dto에 있는 validation어노테이션과 controller @Valid한쌍
    public ResponseEntity<?> save(@Valid @RequestBody AuthorCreateDto authorCreateDto){
//        try{
//            this.authorService.save(authorCreateDto);
//            return new ResponseEntity<>("OK", HttpStatus.CREATED);
//        } catch(IllegalArgumentException e){
//            e.printStackTrace();
//            // 생성자 매개변수 body 부분의 객체와 header 부에 상태코드
//            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(),e.getMessage()) , HttpStatus.BAD_REQUEST);
//        }
        // controllerAdvice가 없었으면 위와 같이 개별적인 예외처리가 필요하나, 이제는 아래와 같이 가능
        this.authorService.save(authorCreateDto);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);

    }
    // 회원목록조회 : /author/list
    @GetMapping("/list")
    public List<AuthorListDto> list(){
        return authorService.findAll();
    }
    // 회원상세조회 : /author/detail/1
    // 서버에서 별도의 try catch 하지 않으면 , 에러발생시 500 에러 + 스프링의 포멧으로 에러발생
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id){
            return new ResponseEntity<>(authorService.findById(id), HttpStatus.CREATED);
    }
    // 비밀번호수정 : email,password -> json
    @PatchMapping("/updatepw")
    public void updatePw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto){
        authorService.updatePassword(authorUpdatePwDto);
    }
    // 회원탈퇴(삭제) : /author/delete/1
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        authorService.delete(id);
    }
}
