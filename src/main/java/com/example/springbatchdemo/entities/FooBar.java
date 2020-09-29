package com.example.springbatchdemo.entities;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
public class FooBar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String foo;
    @Column
    private String bar;

    public FooBar(String foo, String bar) {
        this.foo = foo;
        this.bar = bar;
    }

    public FooBar() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }
}
