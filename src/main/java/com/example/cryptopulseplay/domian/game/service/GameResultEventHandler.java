package com.example.cryptopulseplay.domian.game.service;

import com.example.cryptopulseplay.domian.game.model.GameResultEvent;
import com.example.cryptopulseplay.domian.reword.model.Notification;
import com.example.cryptopulseplay.domian.reword.service.NotificationService;
import com.example.cryptopulseplay.domian.reword.service.RewordService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameResultEventHandler {

    private final RewordService rewordService;
    private final NotificationService notificationService;


    @EventListener
    public void handleEvent(GameResultEvent event) {
        rewordService.createReword(event.getGemaId());
        notificationService.notify(new Notification("Game result is" + event.getOutcome()));
    }




}
