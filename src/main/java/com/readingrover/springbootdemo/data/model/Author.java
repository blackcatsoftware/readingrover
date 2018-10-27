package com.readingrover.springbootdemo.data.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
public class Author
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(nullable = false)
    @JsonProperty("first_name")
    private String first_name;
    
    @Column(nullable = false)
    @JsonProperty("last_name")
    private String last_name;

    
    @JsonProperty("id")
    public long getSimpleId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getSimpleFirstName()
    {
        return first_name;
    }

    public Author setFirstName(String first_name)
    {
        this.first_name = first_name;
        return this;
    }

    public String getSimpleLastName()
    {
        return last_name;
    }

    public Author setLastName(String last_name)
    {
        this.last_name = last_name;
        return this;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (! (obj instanceof Author)) return false;
        
        Author other = (Author) obj;
        
        if (getSimpleId() != other.getSimpleId()) return false;
        if (! Objects.equals(first_name, other.first_name)) return false;
        if (! Objects.equals(last_name, other.last_name)) return false;
        
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("%s %s (%d)", first_name, last_name, id);
    }
}
