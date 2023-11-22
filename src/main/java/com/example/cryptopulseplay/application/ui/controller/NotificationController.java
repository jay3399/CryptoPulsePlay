package com.example.cryptopulseplay.application.ui.controller;


import com.example.cryptopulseplay.domian.reword.model.Notification;
import com.example.cryptopulseplay.domian.reword.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    /**
     * 게임 리워드 지급시 알림.
     * @param userId
     * @return
     */
    @GetMapping(value = "/notifications/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Notification> getUserNotifications(@PathVariable Long userId) {

        Flux<Notification> notification = notificationService.getNotification(userId);

        return notification;
    }

}
