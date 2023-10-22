package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.reword.model.Notification;
import com.example.cryptopulseplay.domian.reword.model.Reword;
import com.example.cryptopulseplay.domian.reword.service.NotificationService;
import com.example.cryptopulseplay.domian.reword.service.RewordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RewordAppService {

    private final RewordService rewordService;
    private final NotificationService notificationService;


    @Transactional
    public void payReword() {

        List<Reword> rewordOnPending = rewordService.findRewordOnPending();

        for (Reword reword : rewordOnPending) {
            reword.applyReword();
            Long userId = reword.getUser().getId();
            Notification notification = new Notification(
                    "you've received reword : " + reword.getAmount(), userId);
            notificationService.notify(notification);
        }

        /**
         * 트렌젝션은 기본적으로 단일 스레드에서 관리된다 .
         * 하지만 병렬스트림은 여러 스레드에서 관리되기떄문에 트랜잭션범위,관리 문제가 발생할수있음.
         */
//        rewords.parallelStream().forEach(Reword::applyReword);

    }


}
