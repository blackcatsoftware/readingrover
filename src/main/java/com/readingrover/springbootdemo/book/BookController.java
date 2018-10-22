package com.readingrover.springbootdemo.book;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController
{
    @Autowired
    private BookRepository repo;
    
    @GetMapping
    public Iterable<Book> findAll()
    {
        return repo.findAll();
    }
    
    @GetMapping("/title/{book_title}")
    public List<Book> findByTyitle(@PathVariable String book_title)
    {
        return repo.findByTitle(book_title);
    }
    
    @GetMapping("/{id}")
    public Book findById(@PathVariable long id)
    {
        return repo.findById(id).orElseThrow(BookNotFoundException::new);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book)
    {
        return repo.save(book);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        repo.findById(id).orElseThrow(BookNotFoundException::new);
        repo.deleteById(id);
    }
    
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book book)
    {
        if (id != book.getSimpleId()) throw new BookIdMismatchException();
        
        repo.findById(id).orElseThrow(BookNotFoundException::new);
        return repo.save(book);
    }
}
