package com.example.springbatchdemo.models;

import java.io.Serializable;

public class Foo implements Serializable {

    private String foo;

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

}
