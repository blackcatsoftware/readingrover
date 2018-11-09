package com.readingrover.springbootdemo.data.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonProperty;

@Projection(types = { Author.class })
public interface NameOnly
{
    @Value("#{target.firstName} #{target.lastName}")
    @JsonProperty("full_name")
    String getFullName();
}
