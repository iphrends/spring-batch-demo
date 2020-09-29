package com.example.springbatchdemo.exceptions;

public class MyException extends RuntimeException {
    public MyException(Throwable e) {
        super(e);
    }
}
