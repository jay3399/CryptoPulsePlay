package com.example.cryptopulseplay.domian.game.service;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.repository.GameRepository;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {


    private final GameRepository gameRepository;

    /**
     * 게임참여
     * @param user
     * @param amount 참여포인트
     * @param direction 유저의 예측방향
     * @return
     */
    public Game startGame(User user, int amount, Direction direction) {

        user.playGame(amount);

        return Game.createGame(user, amount, direction);

    }


//    @Transactional
    public void saveGame(Game game) {
        gameRepository.save(game);
    }


}
