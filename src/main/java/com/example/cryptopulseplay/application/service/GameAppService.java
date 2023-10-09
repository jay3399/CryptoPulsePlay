package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.repository.GameRepository;
import com.example.cryptopulseplay.domian.game.service.GameService;
import com.example.cryptopulseplay.domian.reword.model.Reword;
import com.example.cryptopulseplay.domian.reword.repository.RewordRepository;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameAppService {

    private final GameService gameService;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final RedisUtil redisUtil;

    @Transactional
    @Async
    public CompletableFuture<Game> createGame(Long userId, int amount, Direction direction) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Id" + userId + "not found"));

        Game game = gameService.startGame(user, amount, direction);

        redisUtil.setGameByUser(userId, game);

        return CompletableFuture.completedFuture(game);

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

        for (String gameKey : gameKeys) {

            //게임 추출
            Game game = redisUtil.getGame(gameKey);

            //게임 결과 업데이트
            game.calculateOutcome(direction);

//            // 리워드생성    로직분리 -> 이벤트기반 비동기처리 ,  별도 트렌젝션으로 관리
//            Reword reword = Reword.create(game);
//            rewordRepository.save(reword);

            gameRepository.save(game);
        }

    }


}
