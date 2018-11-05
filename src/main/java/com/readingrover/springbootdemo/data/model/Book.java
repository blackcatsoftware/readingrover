package com.readingrover.springbootdemo.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity(name = "book")
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;
    
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name="book_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="author_id", referencedColumnName="id")
    )
    private List<Author> authors = new ArrayList<>();

    
    @JsonProperty("id")
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle()
    {
        return title;
    }

    public Book setTitle(String title)
    {
        this.title = title;
        return this;
    }

    @JsonProperty("authors")
    public List<Author> getAuthors()
    {
        return authors;
    }

    public Book setAuthors(List<Author> authors)
    {
        this.authors = authors;
        return this;
    }
    
    public Book addAuthor(Author author)
    {
        this.authors.add(author);
        return this;
    }
    
    public Book removeAuthor(Author author)
    {
        this.authors.remove(author);
        return this;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (! (obj instanceof Book)) return false;
        
        Book other = (Book) obj;
        
        if (getId() != other.getId()) return false;
        if (! Objects.equals(title, other.title)) return false;
        if (! Objects.equals(authors, other.authors)) return false;
        
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("\"%s\"(%d) by %s", title, id, StringUtils.collectionToDelimitedString(authors, ", "));
    }
}
