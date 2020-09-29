package com.example.springbatchdemo.writers;

import com.example.springbatchdemo.entities.FooBar;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;
import java.util.function.Consumer;

public class IdpItemWriter implements ItemWriter<List<FooBar>> {

    private final JpaItemWriter<FooBar> jpaItemWriter;

    public IdpItemWriter(JpaItemWriter jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(List<? extends List<FooBar>> items) throws Exception {
        items
                .forEach((Consumer<List<FooBar>>) jpaItemWriter::write);
    }

    /*public IdpItemWriter(EntityManagerFactory factory) {
        setEntityManagerFactory(factory);
    }*/

}
