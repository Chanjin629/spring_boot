package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//mybatis 레파지토리로 만들때 필요하나 어노테이션이다
@Mapper
public interface AuthorMybatisRepository {
    void save(Author author);

    List<Author> findAll();

    Optional<Author> findById(Long id);

    Optional<Author> findByEmail(String email);

    void delete(Long id);
}
