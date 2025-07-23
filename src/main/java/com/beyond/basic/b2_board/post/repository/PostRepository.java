package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    select * from post where author_id = ? and title = ?;
//    List<Post> findByAuthorIdAndTitle(Long authorId, String title);

//    select * from post where author_id = ? and title = ? order by createdTime desc;
//    List<Post> findByAuthorIdAndTitleOrderByCreatedTimeDesc(Long author, String title);

    List<Post> findByAuthorId(Long id);

    // 변수명은 author 지만 authorId로도 조회가능하다
    List<Post> findByAuthor(Author author);

//    jpql 을 사용한 일반 inner join
//    jpql는 기본적으로 lazy 로딩을 지향하므로, inner join 으로 filtering 은 하되 post 객체만 조회 -> N+1 문제 여전히 발생
//    raw쿼리 : select p.* from post p inner join author a on a.id = p.author_id
    @Query("select p from Post p inner join p.author")
    List<Post> findAllJoin();

//    jpql을 사용한 fetch inner join
//    join시 post 뿐만 아니라 author 객체까지 한꺼번에 조립하여 조회 -> N+1 문제 해결
//    raw 쿼리 : select * from post p inner join author a on a.id = p.author_id
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchJoin();

//    paging 처리 + delyn 적용
//    org.springframework.data.domain.Pageable import
//    Page 객체 안에 List<Post>포함, 전체페이지수 등의 정보 포함
//    Pageable 객체 안에는 페이지 size, 페이지번호, 정렬기준 등이 포함
    Page<Post> findAllByDelYnAndAppointment(Pageable pageable, String delYn, String Appointment);

    List<Post> findByAppointment(String appointment);
}
