package com.example.springbatchdemo.processors;

import com.example.springbatchdemo.entities.FooBar;
import com.example.springbatchdemo.models.Foo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.retry.RetryException;

public class FooBarProcessor implements ItemProcessor<Foo, FooBar> {
    private static final Logger log = LoggerFactory.getLogger(FooBarProcessor.class);
    private int counter = 0;
    @Override
    public FooBar process(Foo item) throws Exception {
        ++counter;
//        if (counter == 10) throw new RetryException("RETRY");
        return new FooBar(item.getFoo(), "BAR" + item.getFoo());
        /*return items.stream().map(new Function<Foo, FooBar>() {
            @Override
            public FooBar apply(Foo item) {
                return new FooBar(item.getFoo(), "BAR" + item.getFoo());
            }
        }).collect(Collectors.toList());*/
    }
}

