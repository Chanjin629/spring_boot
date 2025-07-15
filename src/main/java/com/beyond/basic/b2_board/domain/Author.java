package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
public class Author {
    private Long id;
    private String name;
    private String email;
    private String password;

    public Author(String name, String email, String password) {
        this.id = AuthorMemoryRepository.id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public void updatePw(String password){
        this.password = password;
    }

    public AuthorDetailDto detailFromEntity(){
        return new AuthorDetailDto(this.id, this.name, this.email);
    }

    public AuthorListDto listFromEntity(){
        return new AuthorListDto(this.id, this.name, this.email);
    }


}
