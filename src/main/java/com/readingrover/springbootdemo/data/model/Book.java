package com.readingrover.springbootdemo.data.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(nullable = false)
    @JsonProperty("title")
    private String title;
    
    @Column(nullable = false)
    @JsonProperty("author")
    private String author;

    
    @JsonProperty("id")
    public long getSimpleId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getSimpleTitle()
    {
        return title;
    }

    public Book setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public String getSimpleAuthor()
    {
        return author;
    }

    public Book setAuthor(String author)
    {
        this.author = author;
        return this;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (! (obj instanceof Book)) return false;
        
        Book other = (Book) obj;
        
        if (getSimpleId() != other.getSimpleId()) return false;
        if (! Objects.equals(title, other.title)) return false;
        if (! Objects.equals(author, other.author)) return false;
        
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("\"%s\" by %s (%d)", title, author, id);
    }
}
