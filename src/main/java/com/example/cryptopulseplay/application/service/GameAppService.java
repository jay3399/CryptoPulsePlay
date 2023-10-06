package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.repository.GameRepository;
import com.example.cryptopulseplay.domian.game.service.GameService;
import com.example.cryptopulseplay.domian.reword.model.Notification;
import com.example.cryptopulseplay.domian.reword.model.Reword;
import com.example.cryptopulseplay.domian.reword.model.RewordStatus;
import com.example.cryptopulseplay.domian.reword.repository.RewordRepository;
import com.example.cryptopulseplay.domian.reword.service.NotificationService;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameAppService {

    private final GameService gameService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final RewordRepository rewordRepository;
    private final GameRepository gameRepository;
    private final RedisUtil redisUtil;

    @Transactional
    public void createGame(Long userId, int amount, Direction direction) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User Id" + userId + "not found"));

        Game game = gameService.startGame(user, amount, direction);

        redisUtil.setGameByUser(userId, game);

    }

    // 게임결과 생성 , 스트림 고려
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

            //리워드생성
            Reword reword = Reword.create(game);

            //
            rewordRepository.save(reword);
            gameRepository.save(game);
        }

    }

    @Transactional
    public void payReword() {


        List<Reword> rewords = rewordRepository.findAllByRewordStatus(RewordStatus.PENDING);

        for (Reword reword : rewords) {
            reword.applyReword();
            Notification notification = new Notification("you've received reword : " + reword.getAmount());
            notificationService.notify(notification);
        }


        /**
         * 트렌젝션은 기본적으로 단일 스레드에서 관리된다 .
         * 하지만 병렬스트림은 여러 스레드에서 관리되기떄문에 트랜잭션범위,관리 문제가 발생할수있음.
         */
//        rewords.parallelStream().forEach(Reword::applyReword);


    }


}
