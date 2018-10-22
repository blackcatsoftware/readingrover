package com.readingrover.springbootdemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.readingrover.springbootdemo.book.Book;

import io.restassured.RestAssured;
import io.restassured.response.Response;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class LiveTest
{
    static private final String API_ROOT = "http://localhost:8081/api/books";
    static private final Random RANDOM = new Random();
    
    private Book createRandomBook()
    {
        Book book = new Book();
        book.setTitle("Book Title " + RANDOM.nextInt(100));
        book.setAuthor("Author Name " + RANDOM.nextInt(100));
        return book;
    }
    
    private String createBookAsUri(Book book)
    {
        Response response = RestAssured.given()
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .body(book)
                                       .post(API_ROOT);

        return API_ROOT + "/" + response.jsonPath().get("id");
    }
    
    /*
     * TESTS
     */
    
    @Test
    public void whenGetAllBooks_ThenOK()
    {
        Response response = RestAssured.get(API_ROOT);
        
        System.out.println(response.getBody().asString());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
    
    @Test
    public void whenGetBooksByTitle_ThenOK()
    {
        Book book = createRandomBook();
        createBookAsUri(book);
        
        Response response = RestAssured.get(API_ROOT + "/title/" + book.getSimpleTitle());
        
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.as(List.class).size() > 0);
    }
    
    @Test
    public void whenCreatedBookById_ThenOK()
    {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        
        Response response = RestAssured.get(location);
        System.out.println(response.getBody().asString());
        
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(book.getSimpleTitle(), response.jsonPath().get("title"));
    }
    
    @Test
    public void whenGetNotExistBookById_ThenNotFound()
    {
        Response response = RestAssured.get(API_ROOT + "/" + RANDOM.nextInt(1000));
        
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
    
    @Test
    public void whenCreateNewBook_ThenCreated()
    {
        Book book = createRandomBook();
        
        Response response = RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(book)
                        .post(API_ROOT);
        
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }
    
    @Test
    public void whenInvalidBook_ThenError()
    {
        Book book = createRandomBook();
        book.setAuthor(null);
        
        Response response = RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(book)
                        .post(API_ROOT);
        
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }
    
    @Test
    public void whenUpdateCreatedBook_ThenUpdated()
    {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        
        book.setId(Long.parseLong(location.split("api/books/")[1]));
        book.setAuthor("New Author");
        
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
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        
        Response response = RestAssured.delete(location);
        
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        
        response = RestAssured.get(location);
        
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
}
