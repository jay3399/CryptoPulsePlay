package com.example.cryptopulseplay.domian.game.service;

import com.example.cryptopulseplay.application.service.UserAppService;
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
    private final UserAppService userAppService;


    /**
     * 2.GameDTO 를 이벤트에서 가져온다 .
     */

    @EventListener
    public void handleEvent(GameResultEvent event) {
        rewordService.createReword(event.getGame());
        userAppService.finishGameOfUser(event.getUserId());
    }



}
