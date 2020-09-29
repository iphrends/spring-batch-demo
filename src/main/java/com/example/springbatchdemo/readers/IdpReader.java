package com.example.springbatchdemo.readers;

import com.example.springbatchdemo.models.Foo;
import com.google.common.collect.Lists;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class IdpReader implements ItemReader<List<Foo>> {
    private int counter;
    private List<Foo> foos;
    private int chunkSize = 100;

    private void init() {
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.GET, URI.create("http://localhost:8990/test-data"));
        ResponseEntity<Map<String, List<Foo>>> exchange = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Map<String, List<Foo>>>() {
        });
        Map<String, List<Foo>> body = exchange.getBody();
        if (body != null) {
            foos = body.get("list");
        }
        counter = 0;
    }

    @Override
    public List<Foo> read() {
        if (null == foos) init();
        if (null != foos) {
            List<List<Foo>> obj = Lists.partition(foos, chunkSize);
            if (counter < obj.size()) {
                List<Foo> foos = obj.get(counter);
                ++counter;
                return foos;
            } else {
                counter = 0;
            }
        }
        return null;
    }
}
