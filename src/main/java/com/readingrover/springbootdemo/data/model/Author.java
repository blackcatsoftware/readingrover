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
    private String first_name;
    
    @Column(nullable = false)
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

    @JsonProperty("first_name")
    public String getSimpleTitle()
    {
        return first_name;
    }

    public void setFirstName(String first_name)
    {
        this.first_name = first_name;
    }

    @JsonProperty("last_name")
    public String getSimpleAuthor()
    {
        return last_name;
    }

    public void setLastName(String last_name)
    {
        this.last_name = last_name;
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
