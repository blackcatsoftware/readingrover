package com.readingrover.springbootdemo.data.repos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.readingrover.springbootdemo.data.model.Author;


public interface AuthorRepository extends PagingAndSortingRepository<Author, Long>
{
    @Query("FROM author a "
         + "WHERE LOWER(a.first_name) LIKE LOWER(CONCAT('%', :name, '%')) "
            + "OR LOWER(a.last_name) LIKE LOWER(CONCAT('%', :name, '%'))"
         + "ORDER BY a.first_name, a.last_name")
    List<Author> findByName(@Param("name") String name);
//    List<Author> findByName(@Param("name") String name, Sort sort);
}
