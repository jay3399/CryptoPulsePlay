package com.example.cryptopulseplay.application.ui.controller;


import com.example.cryptopulseplay.domian.reword.model.Notification;
import com.example.cryptopulseplay.domian.reword.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping(value = "/notifications" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Notification> getUserNotifications() {
        return notificationService.getNotification();
    }

}
