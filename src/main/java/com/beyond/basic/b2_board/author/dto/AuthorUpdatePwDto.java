package com.beyond.basic.b2_board.author.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorUpdatePwDto {
    private String email;
    private String password;
}
