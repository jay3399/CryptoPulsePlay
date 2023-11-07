package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.model.GameResultEvent;
import com.example.cryptopulseplay.domian.game.service.GameService;
import com.example.cryptopulseplay.domian.pricerecord.model.PriceRecord;
import com.example.cryptopulseplay.domian.reword.service.RewordService;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.shared.service.DomainEventPublisher;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.service.UserService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameAppService {

    private final GameService gameService;
    private final UserService userService;
    private final RewordService rewordService;
    private final RedisUtil redisUtil;
    private final DomainEventPublisher domainEventPublisher;

    private final EntityManager entityManager;


    @Transactional
    public void createGame(Long userId, int amount, Direction direction) {

        User user = userService.findUser(userId);

        Game game = gameService.startGame(user, amount, direction);

        redisUtil.setGameByUser(userId, game);

    }

    // 매시 59분 59초 스케줄

    // 게임결과 생성 , 스트림 고려

    /**
     * 모든 게임데이터를 순회하고 , 로직을 수행하고 , 디비에 저장하는 작업 if 대용량 -> 과부화를 일으킬수있다 -> Spring Batch 적용 고려 . Chunk
     * Processing - 게임 데이터를 일정크기의 청크로 나누어 , 청크마다 트랜잭션을 관리 Parallel Processing - 여러 스레드를사용 , 각 청크를
     * 병렬로 처리 Retry - 실패한 작업에대해 재시도 or 특정 횟수이상 실패하면 건너뛰는 로직 실행
     */

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
     *
     * -> 문제점 , 이벤트가 발행될떄마다 디비에 save 가 날라간다.
     */
    @Transactional
    public void calculateGameResult(Direction direction) {

        // 키 추출
        Set<String> gameKeys = redisUtil.gameKeys();

        if (gameKeys == null) {
            return;
        }

        for (String gameKey : gameKeys) {

            Game game = redisUtil.getGame(gameKey);

            GameResultEvent gameResultEvent = game.calculateOutcome(direction);

            domainEventPublisher.publish(gameResultEvent);
        }

    }



    /**
     * 이벤트헨들러 전략 사용 x 트렌젝션 문제때문에 사용하기가 번거로움 + 카운트 기능까지 포함하려면 더더욱.
     * <p>
     * but , 아래처럼 전체에다가 트랜잭션을 걸면 범위가 너무 넓다 !! 1. 롤백 문제 , 마지막에서 예외가생겨도 전체롤백 2. 성능문제 , 큰범위의 트랜잭션은
     * 데이터베이스의 잠금시간을 늘릴수있다 -> 다른 트랜젝션들에대한 대기시간을 증가시킬수가있음.
     * <p>
     * 보상생성 부분을 따로 별도의 트랜젝션으로 메서드를 분리해서 적용한다
     */

    @Transactional
    public void calculateGameResultV2(PriceRecord priceRecord) {

        Set<String> gameKeys = redisUtil.gameKeys();

        if (gameKeys == null) {
            return;
        }

        for (String gameKey : gameKeys) {
            // 게임추출
            Game game = redisUtil.getGame(gameKey);
            // 해당 게임의 에측방향을 가져옴
            Direction userDirection = game.getDirection();
            // 해당 게임의 예측방향과 , 실제 해당회 게임의 실제 방향을 비교후 게임의 결과를 업데이트
            game.calculateOutcome(priceRecord.getDirection());
            // 유저의 방향을 가져와서, priceRecord 에 종합적으로 해당 회차 게임의 모든 투표 결과를 집계.
            countOnVoting(userDirection, priceRecord);
            // 게임을 끝냄 , 해당 게임에 해당하는 리워드를 만들고 , 유저의 게임참개 상태를 false 로 업데이트.
            // -> 해당부분을 별도의 트랭ㄴ잭션으로 분리
            finishGame(game);
        }


    }

    public void calculateGameResultV3(PriceRecord priceRecord) {

        Set<String> gameKeys = redisUtil.gameKeys();

        if (gameKeys == null) {
            return;
        }

        List<Game> gamesToReword = new ArrayList<>();

        for (String gameKey : gameKeys) {
            // 게임추출
            Game game = redisUtil.getGame(gameKey);
            // 해당 게임의 에측방향을 가져옴
            Direction userDirection = game.getDirection();
            // 해당 게임의 예측방향과 , 실제 해당회 게임의 실제 방향을 비교후 게임의 결과를 업데이트
            game.calculateOutcome(priceRecord.getDirection());
            // 유저의 방향을 가져와서, priceRecord 에 종합적으로 해당 회차 게임의 모든 투표 결과를 집계.
            countOnVoting(userDirection, priceRecord);
            // 게임을 끝냄 , 해당 게임에 해당하는 리워드를 만들고 , 유저의 게임참개 상태를 false 로 업데이트.
            // -> 해당부분을 별도의 트랭ㄴ잭션으로 분리
        }

        finishGameV2(gamesToReword);


    }


    private void finishGame(Game game) {
        createRewordForGame(game);
        finishGameForUser(game.getUser().getId());
    }


    /**
     * 배치처리 적용
     */

    @Transactional
    public void finishGameV2(List<Game> games) {

        int batchSize = 50;
        int count = 0;

        for (Game game : games) {
            createRewordForGame(game);
            finishGameForUser(game.getUser().getId());

            if (++count % batchSize == 0) {

                entityManager.flush();
                entityManager.clear();

            }
        }

        entityManager.flush();
    }


    private void createRewordForGame(Game game) {
        rewordService.createReword(game);
    }

    private void finishGameForUser(Long userId) {
        User user = userService.findUser(userId);
        user.finishGame();
    }

    private void countOnVoting(Direction userDirection, PriceRecord priceRecord) {

        if (userDirection == Direction.UP) {
            priceRecord.increaseLongCount();
        } else {
            priceRecord.increaseShortCount();
        }


    }


}
