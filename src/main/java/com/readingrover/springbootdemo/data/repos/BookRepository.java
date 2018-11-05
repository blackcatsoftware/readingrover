package com.readingrover.springbootdemo.data.repos;

import java.util.List;

import javax.persistence.OrderBy;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.readingrover.springbootdemo.data.model.Author;
import com.readingrover.springbootdemo.data.model.Book;


public interface BookRepository extends PagingAndSortingRepository<Book, Long>
{
    @Query("FROM book b "
         + "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :text, '%')) "
         + "ORDER BY b.title")
//            + "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :text, '%'))")
    List<Book> findByTitle(@Param("text") String text);
    
    @OrderBy("title")
    List<Book> findByAuthorsIn(Author author);
}
