package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostCreateDto {
    @NotEmpty
    private String title;
    private String contents;
    private String category;
//    @NotNull // 숫자는 NotEmpty 사용불가
//    private Long authorId;
    @Builder.Default
    private String appointment = "N";
//    시간정보는 직접 LocalDateTime으로 형변환하는 경우가 많음
    private String appointmentTime;

    public Post toEntity(Author author, LocalDateTime appointmentTime) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
//                .authorId(this.authorId)
                .category(this.category)
                .author(author)
                .delYn("N")
                .appointment(this.appointment)
                .appointmentTime(appointmentTime)
                .build();
    }

}
