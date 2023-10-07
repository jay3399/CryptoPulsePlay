package com.example.cryptopulseplay.domian.shared.service;

import org.springframework.context.ApplicationEventPublisher;

public class DomainEventPublisher {


    private ApplicationEventPublisher publisher;


    private static final DomainEventPublisher INSTANCE = new DomainEventPublisher();

    public static DomainEventPublisher getInstance() {
        return INSTANCE;
    }

    public void setPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(Object event) {
        this.publisher.publishEvent(event);
    }



}
