package com.example.springbatchdemo.readers;

import com.example.springbatchdemo.exceptions.MyException;
import com.example.springbatchdemo.models.Foo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class IdpReader implements ItemReader<Foo> {
    private static final Logger log = LoggerFactory.getLogger(IdpReader.class);
    private int counter;
    private List<Foo> foos = new ArrayList<>();
    private final int chunkSize = 800;
    int from = 0;
    int to = chunkSize;
    boolean alreadyInitiated = false;

    public void init() {
        if (null != foos) {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setReadTimeout(5);
            RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(factory));
            URI url = URI.create("http://localhost:8990/test-data?from=" + from + "&to=" + to);
            try {
                RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.GET, url);
                ResponseEntity<Map<String, List<Foo>>> exchange = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Map<String, List<Foo>>>() {
                });
                Map<String, List<Foo>> body = exchange.getBody();
                if (body != null) {
                    foos = body.get("list");
                    log.info("{} -> list.size():{}", url.toString(), foos.size());
                }
                counter = 0;
            } catch (Exception e) {
                throw new MyException(e);
            }
            from += chunkSize;
            to += chunkSize;
        }
    }

    @Override
    @Retryable(maxAttempts = 5, listeners = {"retryListener"})
    public Foo read() {
        if (null == foos) return null;
        if (foos.isEmpty()) init();
        if (null != foos) {
            if (counter < foos.size()) {
                Foo foo = foos.get(counter);
                counter++;
                return foo;
            } else if (foos.size() < chunkSize) foos = null;
            else {
                foos.clear();
            }
        }
        return new Foo();
    }
}
