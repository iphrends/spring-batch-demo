package com.example.springbatchdemo.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

public class RetryListner extends RetryListenerSupport {
    private final Logger log = LoggerFactory.getLogger(RetryListner.class);

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.warn("Retry attempt {} for retryable method {} threw exception {}", context.getRetryCount(),
                context.getAttribute("context.name"), throwable.getStackTrace());
        super.onError(context, callback, throwable);
    }
}
