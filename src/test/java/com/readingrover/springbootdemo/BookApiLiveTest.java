package com.readingrover.springbootdemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.readingrover.springbootdemo.data.model.Author;
import com.readingrover.springbootdemo.data.model.Book;

import io.restassured.RestAssured;
import io.restassured.response.Response;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class BookApiLiveTest
{
    static private final String API_ROOT_BOOKS = "http://localhost:8081/api/books";
    static private final String API_ROOT_AUTHORS = "http://localhost:8081/api/authors";
    static private final Random RANDOM = new Random();
    
    
    static private Author author1;
    static private Author author2;
    
    
    static private Author createRandomAuthor()
    {
        Author author = new Author();
        author.setFirstName("First Name " + RANDOM.nextInt(100));
        author.setLastName("Last Name " + RANDOM.nextInt(100));
        return author;
    }
    
    static private String createAuthorAsUri(Author author)
    {
        Response response = RestAssured.given()
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .body(author)
                                       .post(API_ROOT_AUTHORS);

        return API_ROOT_AUTHORS + "/" + response.jsonPath().get("id");
    }
    
    static private Book createRandomBook(Author author)
    {
        Book book = new Book();
        book.setTitle("Book Title " + RANDOM.nextInt(100));
        book.addAuthor(author);
        return book;
    }
    
    static private String createBookAsUri(Book book)
    {
        Response response = RestAssured.given()
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .body(book)
                                       .post(API_ROOT_BOOKS);

        return API_ROOT_BOOKS + "/" + response.jsonPath().get("id");
    }
    
    @Before
    public void setup()
    {
        if (null == author1)
        {
            author1 = createRandomAuthor();
            createAuthorAsUri(author1);
        }
        
        if (null == author2)
        {
            author2 = createRandomAuthor();
            createAuthorAsUri(author2);
        }
    }
    
    /*
     * TESTS
     */
    
    @Test
    public void whenGetAllBooks_ThenOK()
    {
        Response response = RestAssured.get(API_ROOT_AUTHORS);
        
        System.out.println(response.getBody().asString());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
    
    @Test
    public void whenGetBooksByTitle_ThenOK()
    {
        Book book = createRandomBook(author1);
        createBookAsUri(book);
        
        Response response = RestAssured.get(API_ROOT_AUTHORS + "/title/" + book.getTitle());
        
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.as(List.class).size() > 0);
    }
    
    @Test
    public void whenCreatedBookById_ThenOK()
    {
        Book book = createRandomBook(author1);
        String location = createBookAsUri(book);
        
        Response response = RestAssured.get(location);
        System.out.println(response.getBody().asString());
        
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(book.getTitle(), response.jsonPath().get("title"));
    }
    
    @Test
    public void whenGetNotExistBookById_ThenNotFound()
    {
        Response response = RestAssured.get(API_ROOT_AUTHORS + "/" + RANDOM.nextInt(1000));
        
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
    
    @Test
    public void whenCreateNewBook_ThenCreated()
    {
        Book book = createRandomBook(author1);
        
        Response response = RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(book)
                        .post(API_ROOT_AUTHORS);
        
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }
    
    @Test
    public void whenInvalidBook_ThenError()
    {
        Book book = createRandomBook(author1);
        book.setAuthors(null);
        
        Response response = RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(book)
                        .post(API_ROOT_AUTHORS);
        
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }
    
    @Test
    public void whenUpdateCreatedBook_ThenUpdated()
    {
        Book book = createRandomBook(author1);
        String location = createBookAsUri(book);
        
        book.setId(Long.parseLong(location.split("api/books/")[1]));
        book.addAuthor(author2);
        
        Response response = RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(book)
                        .put(location);
        
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("New Author", response.jsonPath().get("author"));
        
        response = RestAssured.get(location);
        
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("New Author", response.jsonPath().get("author"));
    }
    
    @Test
    public void whenDeleteBook_ThenDeleted()
    {
        Book book = createRandomBook(author1);
        String location = createBookAsUri(book);
        
        Response response = RestAssured.delete(location);
        
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        
        response = RestAssured.get(location);
        
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
}
