package com.readingrover.springbootdemo.data.api.author;

public class AuthorNotFoundException extends RuntimeException
{
    public AuthorNotFoundException() {}
    
    public AuthorNotFoundException(String message)
    {
        super(message);
    }
    
    public AuthorNotFoundException(Throwable cause)
    {
        super(cause);
    }
    
    public AuthorNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public AuthorNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
