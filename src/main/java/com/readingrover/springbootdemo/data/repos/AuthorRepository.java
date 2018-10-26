package com.readingrover.springbootdemo.data.repos;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.readingrover.springbootdemo.data.model.Author;


public interface AuthorRepository extends CrudRepository<Author, Long>
{
    // Search both first_name and last_name
    // Returns any matches to _either_ field
    @Query("From Author a WHERE a.first_name LIKE :name OR a.last_name LIKE :name")
    List<Author> findByName(@Param("name") String name, Sort sort);
}
