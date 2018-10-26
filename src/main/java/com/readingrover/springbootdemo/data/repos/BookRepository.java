package com.readingrover.springbootdemo.data.repos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.readingrover.springbootdemo.data.model.Book;


public interface BookRepository extends CrudRepository<Book, Long>
{
    @Query("FROM Book b "
         + "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :text, '%')) "
            + "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :text, '%'))")
    List<Book> findByTextSearch(@Param("text") String text);
}
