package com.relay.relay.exception;

public class RateLimitExceededException implements RuntimeException{
    public RateLimitExceededException(String message){
        super(message);
    }
}
