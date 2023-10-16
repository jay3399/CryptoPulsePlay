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

        System.out.println("알림 !!!! ");

        Flux<Notification> notification = notificationService.getNotification();

        notification.doOnNext(
                notification1 -> System.out.println(notification1.getMessage())
        ).subscribe();

        return notificationService.getNotification();

    }

}
