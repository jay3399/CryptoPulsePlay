package com.example.cryptopulseplay.domian.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainEventPublisher {


    private final ApplicationEventPublisher publisher;


    public void publish(Object event) {
        this.publisher.publishEvent(event);
    }


}
