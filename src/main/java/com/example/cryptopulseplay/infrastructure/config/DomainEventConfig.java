package com.example.cryptopulseplay.infrastructure.config;

import com.example.cryptopulseplay.domian.shared.service.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventConfig {

    @Autowired
    public void configure(ApplicationEventPublisher applicationEventPublisher) {
        DomainEventPublisher.getInstance().setPublisher(applicationEventPublisher);
    }

}
