package com.example.springbatchdemo.config;

import com.example.springbatchdemo.entities.FooBar;
import com.example.springbatchdemo.models.Foo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.json.GsonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.net.MalformedURLException;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

    @Bean
    public ItemReader<Foo> reader() throws MalformedURLException {
        Resource resource = new UrlResource("http://localhost:8990/test-data/list");
        return new JsonItemReader<>(resource, new GsonJsonObjectReader<>(Foo.class));
    }

    @Bean
    public FooBarProcessor processor() {
        return new FooBarProcessor();
    }

    @Bean
    public JpaItemWriter<FooBar> writer() {
        JpaItemWriter<FooBar> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        return writer;
    }

    @Bean
    public Step step1() throws MalformedURLException {
        return stepBuilderFactory.get("step1")
                .<Foo, FooBar>chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job importUserJob(Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    /*@Bean
    public Job importVoltageJob(NotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importVoltageJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }*/
}

class FooBarProcessor implements ItemProcessor<Foo, FooBar> {

    @Override
    public FooBar process(Foo item) throws Exception {
        return new FooBar(item.getFoo(), "BAR" + item.getFoo());
        /*return items.stream().map(new Function<Foo, FooBar>() {
            @Override
            public FooBar apply(Foo item) {
                return new FooBar(item.getFoo(), "BAR" + item.getFoo());
            }
        }).collect(Collectors.toList());*/
    }
}

