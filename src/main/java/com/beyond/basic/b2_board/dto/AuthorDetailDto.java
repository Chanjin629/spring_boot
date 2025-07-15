package com.beyond.basic.b2_board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorDetailDto {
    private Long id;
    private String name;
    private String email;
}
