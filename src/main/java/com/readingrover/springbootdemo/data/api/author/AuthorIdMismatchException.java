package com.readingrover.springbootdemo.data.api.author;

public class AuthorIdMismatchException extends RuntimeException
{
    public AuthorIdMismatchException() {}
    
    public AuthorIdMismatchException(String message)
    {
        super(message);
    }
    
    public AuthorIdMismatchException(Throwable cause)
    {
        super(cause);
    }
    
    public AuthorIdMismatchException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public AuthorIdMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
