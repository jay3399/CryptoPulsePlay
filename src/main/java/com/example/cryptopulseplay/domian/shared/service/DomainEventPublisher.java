package com.example.cryptopulseplay.domian.shared.service;

import org.springframework.context.ApplicationEventPublisher;

public class DomainEventPublisher {


    private ApplicationEventPublisher publisher;

    // 싱글턴패턴 , 어플리케이션 전체엘서 하나의 인스턴스만을 사용.
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
