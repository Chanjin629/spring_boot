package com.beyond.basic.b1_hello.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Getter // 클래스내의 모든 변수를 대상으로 getter 생성
@Data // gettet, setter, toString 메서드까지 모두 만들어주는 어노테이션
@AllArgsConstructor // 모든매개변수가있는 생성자
@NoArgsConstructor // 기본생성자
// 기본생성자와 + getter로 parsing이 이뤄지므로 보통은 필수적 요소
public class Hello {
    private String name;
    private String email;
//    private MultipartFile photo;

}
