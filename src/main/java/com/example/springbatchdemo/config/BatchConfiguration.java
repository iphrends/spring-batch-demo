package com.example.springbatchdemo.config;

import com.example.springbatchdemo.entities.FooBar;
import com.example.springbatchdemo.listeners.RetryListner;
import com.example.springbatchdemo.models.Foo;
import com.example.springbatchdemo.processors.FooBarProcessor;
import com.example.springbatchdemo.readers.IdpReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;

import java.net.MalformedURLException;

@Configuration
@EnableBatchProcessing
@EnableRetry
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

    @Bean
    public RetryListener retryListener() {
        return new RetryListner();
    }

    @Bean
    public ItemReader<Foo> reader() throws MalformedURLException {
        /*Resource resource = new UrlResource("http://localhost:8990/test-data/list");
        return new JsonItemReader<>(resource, new GsonJsonObjectReader<>(Foo.class));*/
        return new IdpReader();
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
                .<Foo, FooBar>chunk(100)
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
