package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.reword.model.Notification;
import com.example.cryptopulseplay.domian.reword.model.Reword;
import com.example.cryptopulseplay.domian.reword.model.RewordStatus;
import com.example.cryptopulseplay.domian.reword.service.NotificationService;
import com.example.cryptopulseplay.domian.reword.service.RewordService;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RewordAppService {

    private final RewordService rewordService;
    private final NotificationService notificationService;
    private final EntityManager entityManager;

    private static final int BATCH_SIZE = 100;

    private static final int PAGE_SIZE = 1000;


    @Transactional
    public void payReword() {

        List<Reword> rewordOnPending = rewordService.findRewordOnPending();

        for (Reword reword : rewordOnPending) {
            reword.applyReword();
            Long userId = reword.getUser().getId();
            Notification notification = new Notification("you've received reword : " + reword.getAmount(), userId);
            notificationService.notify(notification);
        }


    }


    /**
     * for loop 내부에 리워드지급로직메서드를 생성 분리 하여,  트랜잭션 범위를 줄일수있다.
     * <p>
     * 하지만 , 그렇게하면 JPA 의 지연쓰기 기능을 사용하지못한다.
     * <p>
     * 그렇다고 아래처럼 트랜잭션을 넓게잡아주면 , 하나의 리워드처리에서문제가 발생할시 전체트랜잭션이 롤백될 위험이있다.
     * <p>
     * 두 문제 같이 해결하고싶다
     * <p>
     * -> 작은 트랜잭션 단위로 나누면서 + 배치처리를 이용해서 해겷한다.
     */


    @Transactional
    public void payRewordV2() {

        List<Reword> rewordOnPending = rewordService.findRewordOnPending();

        processRewords(rewordOnPending);

    }

    @Transactional
    public void payRewordV3() {

        /**
         * 페이징처리를 이용해서 , 만약 Pending 상태 reword 데이터가 많은경우를 고려한다
         */

        int pageNumber = 0;
        Page<Reword> rewordPage;

        do {rewordPage = rewordService.findRewordOnPendingV2(RewordStatus.PENDING, PageRequest.of(pageNumber, PAGE_SIZE));
            processRewords(rewordPage.getContent());
            pageNumber++;
        } while (rewordPage.hasNext());




    }

    private void processRewords(List<Reword> rewords) {

        int count = 0;

        for (Reword reword : rewords) {
            reword.applyReword();
            Long id = reword.getUser().getId();
            Notification notification = new Notification("you've received reword : " + reword.getAmount(), id);
            notification.notify();

            if (++count % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }

            entityManager.flush();
            entityManager.clear();

        }

    }


}


/**
 * 트렌젝션은 기본적으로 단일 스레드에서 관리된다 .
 * 하지만 병렬스트림은 여러 스레드에서 관리되기떄문에 트랜잭션범위,관리 문제가 발생할수있음.
 */
//        rewords.parallelStream().forEach(Reword::applyReword);
