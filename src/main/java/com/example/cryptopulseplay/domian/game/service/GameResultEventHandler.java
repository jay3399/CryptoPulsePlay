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
    private final NotificationService notificationService;


    /**
     * 트렌젝션을 , 도메인서비스에 걸어줄지 , 응용서비스에 할지 고민을 해봐야한다.
     */

    @EventListener
    public void handleEvent(GameResultEvent event) {
        rewordService.createReword(event.getGemaId());
        userAppService.finishGameOfUser(event.getUserId());
        notificationService.notify(new Notification("Game result is" + event.getOutcome()));
    }



}
