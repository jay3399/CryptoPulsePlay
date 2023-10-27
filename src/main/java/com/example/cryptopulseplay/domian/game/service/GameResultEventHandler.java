package com.example.cryptopulseplay.domian.game.service;

import com.example.cryptopulseplay.application.service.UserAppService;
import com.example.cryptopulseplay.domian.game.model.GameResultEvent;
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
     *
     * 이벤트 헨들러가 redis 에 저장돼있는 gmae 수먼큼 호출이되고 , 호출 할떄마다 디비에 접근해서 저장로직을 실행한다
     *
     * -> JPA 는 기본적으로 하나의 트렌젝션에서는 일괄커밋기능을 제공한다
     *
     * -> BUT , 해당 이벤트헨들러는 기본적으로 호출하는 쪽과는 별도의 트렌젝션으로 동작을 한다 그러므로 이벤트헨들러를 사용하지 않는선에서만 해당 기능을 사용할수있다.
     *
     *  Hibernate Batch Processing 기능을 사용해보면 어떨까 ?
     *
     *  -> 위와 똑같다 , 결국 같은 트렌젝션에서 관리 되어야한다
     *
     *  큐를 이용한다
     *  1. 게임 결과 이벤트를 메세지큐에 일시적으로 저장한다
     *  2. 큐의크기가 특정임계값에 도달하면 큐에 저장된 게임결과를 큐의크기가 특정 임계깞에 도달할시 디비에 저장한다
     *  3. 해당 방법은 , 큐에저장된 데이터를 한번에 일괄로 처리하기떄문에 트렌젝션 문제도 없을것이다 . ?
     *
     *  하지면 결국 위 처럼 큐를 사용시 , 이벤트핸들러 자체가 필요가 없어진다
     *
        내가 이벤트 헨들러를 이용한방법은 , 책임을 분리하기 위해서였지만 성능적인 문제떄문에 좋아보이지 않는다 .

     *  결론 - 이벤트핸들러 쓰지않는다.
     *
     *
     *
     *

     *
     *
     *
     *
     *
     */
    @EventListener
    public void handleEvent(GameResultEvent event) {
        rewordService.createReword(event.getGame());
        userAppService.finishGameOfUser(event.getUserId());
    }



}
