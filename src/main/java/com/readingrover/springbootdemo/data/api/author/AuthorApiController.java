package com.readingrover.springbootdemo.data.api.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.readingrover.springbootdemo.data.model.Author;
import com.readingrover.springbootdemo.data.repos.AuthorRepository;


@RestController
@RequestMapping("/api/authors")
public class AuthorApiController
{
    @Autowired
    private AuthorRepository repo;
    
    @GetMapping
    public Iterable<Author> search(@RequestParam(value="q", required=false) String text)
    {
        if (null == text || text.isEmpty())
        {
            return repo.findAll();
        }
        else
        {
            return repo.findByName(text);
        }
    }
    
    @GetMapping("/{id}")
    public Author findById(@PathVariable Long id)
    {
        return repo.findById(id).orElseThrow(AuthorNotFoundException::new);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Author create(@RequestBody Author author)
    {
        System.out.println(author);
        return repo.save(author);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        repo.findById(id).orElseThrow(AuthorNotFoundException::new);
        repo.deleteById(id);
    }
    
    @PutMapping("/{id}")
    public Author update(@PathVariable Long id, @RequestBody Author Author)
    {
        if (id != Author.getSimpleId()) throw new AuthorIdMismatchException();
        
        repo.findById(id).orElseThrow(AuthorNotFoundException::new);
        return repo.save(Author);
    }
}
