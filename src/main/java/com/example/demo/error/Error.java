package com.example.demo.error;

public class Error extends RuntimeException{
    public Error(String message){
        super(message);
    }
    public Error(String message,Throwable cues){
        super(message,cues);
    }
}
