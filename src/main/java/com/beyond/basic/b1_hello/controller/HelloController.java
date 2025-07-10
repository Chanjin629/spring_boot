package com.beyond.basic.b1_hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// Component 어노테이션을 통해 별도의 객체를 생성할 필요가없는 , 싱글톤 객체 생성
// Controller 어노테이션을 통해 http req를 분석하고, http res를 생성
@Controller
// 클래스차원에 url 매핑시에는 RequestMapping 을 사용
@RequestMapping("/hello")
public class HelloController {
//  get 요청의 case들
//  case1. 서버가 사용자에게 단순 String 데이터 return - @ResponseBody 있을때
    @GetMapping("") // 아래 메서드에 대한 서버의 엔드포인트 설정
//  ResponseBody가 없고, return 타입이 String인 경우 서버는 templates 폴더밑에 helloworld.html을 찾아서 리턴
    @ResponseBody
    public String helloWorld(){
        return "helloworld";
    }
//  case2. 서버가 사용자에게 String(josn형식)의 데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Hello helloJson() throws JsonProcessingException {
        Hello h1 = new Hello("hong", "hong@naver.com");
//        직접 json으로 직렬화 할필요 없이, return 타입에 객체가 있으면 자동으로 직렬화
//        ObjectMapper mapper = new ObjectMapper();
//        String result = objectMapper.writeValueAsString(h1);
        return h1;
    }
//  case3. parameter 방식을 통해 사용자로부터 값을 수신
//  parameter의 형식 : /member?name=hongildong
    @GetMapping("/param")
    @ResponseBody
    public Hello param(@RequestParam(value = "name")String inputName){
        Hello h1 = new Hello(inputName, "hong@naver.com");
        return h1;
    }

//  case4. pathvariable 방식을 통해 사용자로부터 값을 수신
//  pathvariable 의 형식 : /member/1
//  pathvariable 방식은 url을 통해 자원의 구조를 명확하게 표현할때 사용(좀더 restful함)
    @GetMapping("/path/{inputId}")
    @ResponseBody
    public String path(@PathVariable Long inputId){
//      별도의 형변환 없이도, 매개변수에 타입지정시 자동현변환 시켜줌
//        long id = Long.parseLong(inputId);
        System.out.println(inputId);
        return "OK";
    }

//   case5. parameter 2개 이상 형식
//  /hello/param2?name=hong&email=hong@naver.com
    @GetMapping("/param2")
    @ResponseBody
    public String param2(@RequestParam(value="name")String inputName, @RequestParam(value="email")String inputEmail){
        return "OK";
    }
}
