package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.model.GameResultEvent;
import com.example.cryptopulseplay.domian.game.service.GameService;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.shared.service.DomainEventPublisher;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameAppService {

    private final GameService gameService;
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final DomainEventPublisher domainEventPublisher;


    /**
     * 1.redis 에 gameDTO 를 저장
     */


//    @Async
    @Transactional
    public void createGame(Long userId, int amount, Direction direction) {

        User user = userService.findUser(userId);

        Game game = gameService.startGame(user, amount, direction);

        redisUtil.setGameByUser(userId, game);

    }

    // 매시 59분 59초 스케줄

    // 게임결과 생성 , 스트림 고려

    /**
     * 모든 게임데이터를 순회하고 , 로직을 수행하고 , 디비에 저장하는 작업
     * if 대용량 -> 과부화를 일으킬수있다
     * -> Spring Batch 적용 고려 .
     * Chunk Processing - 게임 데이터를 일정크기의 청크로 나누어 , 청크마다 트랜잭션을 관리
     * Parallel Processing - 여러 스레드를사용 , 각 청크를 병렬로 처리
     * Retry - 실패한 작업에대해 재시도 or 특정 횟수이상 실패하면 건너뛰는 로직 실행
     *
     */


    @Transactional
    public void calculateGameResult(Direction direction) {

        // 키 추출
        Set<String> gameKeys = redisUtil.gameKeys();

        if (gameKeys == null) {
            return;
        }

        /**
         * 목표 :
         * Game 을 영속화 시키지않고 Redis 에 저장
         * 그리고 게임결과를 내는 시점에서 -> 이벤트발행 ->  해당 게임에대한 리워드를 저장
         *
         * 이전 : 이벤트를 발행 -> gameId 를넣고 -> gameId를 가지고 다시 게임 객체를 디비에서 가져온뒤 리워드를 생성
         * -> 애초에 영속화된 상태가 아니라 ID 조차 존재하지않음, 물론 디비에도 없는상태
         *
         * 이후:
         * 1. 영속화 시키지않고 Redis 에 저장하는것은 그대로 유지 -> gameID는 영속화를 못시키니 그냥 Game 자체를 이벤트객체에 넘겨준다
         * 2. 이벤트가 발행되면 , 이벤트에 게임객체를 넣은뒤 해당 게임객체를 리워드를 생성할때 전파 기능을 이용해서 한번에 영속화시킨다.
         *
         * + 이벤트발행로직을 도메인객체에서 분리하고 , 도메인은 이벤트객체만을 발행한다.
         */

        for (String gameKey : gameKeys) {

            //게임 추출
            Game game = redisUtil.getGame(gameKey);
            //게임 결과 업데이트

            //해당시점에서는 game은 영속화가 되어있지 않아서 , gameId 를 반환 해도 Null로 반환이된다.
            GameResultEvent gameResultEvent = game.calculateOutcome(direction);

            domainEventPublisher.publish(gameResultEvent);
            //이떄서야 , 영속화가 되기떄문에 , 위 publish에서 gameId를 받아서 game을 찾은뒤 reword를 생성하려해도 의미가 없어짐 .

            //game을 reword를 생성하는 시점에 전파를 이용해서 한번에 영속화를 시키는 방법을 이용한다.
//            gameService.saveGame(game);

//            Reword reword = Reword.create(game);
//            rewordRepository.save(reword);


        }

    }


}
