package com.readingrover.springbootdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.readingrover.springbootdemo.data.model.Author;
import com.readingrover.springbootdemo.data.model.Book;
import com.readingrover.springbootdemo.data.repos.AuthorRepository;
import com.readingrover.springbootdemo.data.repos.BookRepository;


@Component
public class StartupData
{
    @Component
    static public class BookData implements ApplicationRunner
    {
        @Autowired
        private BookRepository repo;
        
        @Override
        public void run(ApplicationArguments args) throws Exception
        {
//            Book book;
//            book = new Book()
//                       .setTitle("Moby-Dick")
//                       .addAuthor(author);
//            repo.save(book);
//            
//            book = new Book()
//                       .setAuthor("Chester Gould")
//                       .setTitle("Dick Tracy");
//            repo.save(book);
//            
//            book = new Book()
//                       .setAuthor("Jane Austen")
//                       .setTitle("Emma");
//            repo.save(book);
//            
//            book = new Book()
//                       .setAuthor("Jane Austen")
//                       .setTitle("Sense and Sensibility");
//            repo.save(book);
        }
    }
    
    @Component
    static public class AuthorData implements ApplicationRunner
    {
        @Autowired
        private AuthorRepository repo;
        
        @Override
        public void run(ApplicationArguments args) throws Exception
        {
            Author author;
            author = new Author()
                         .setFirstName("Herman")
                         .setLastName("Melville");
            repo.save(author);
            
            author = new Author()
                         .setFirstName("Jane")
                         .setLastName("Austen");
            repo.save(author);
            
            author = new Author()
                         .setFirstName("Chester")
                         .setLastName("Gould");
            repo.save(author);
            
            author = new Author()
                         .setFirstName("Walt")
                         .setLastName("Whitman");
            repo.save(author);
        }
    }
}
