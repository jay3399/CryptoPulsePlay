package com.example.cryptopulseplay.domian.game.service;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.repository.GameRepository;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {


    private final GameRepository gameRepository;

    public Game startGame(User user, int amount, Direction direction) {

        user.playGame(amount);

        return Game.createGame(user, amount, direction);

    }


    @Transactional
    public void saveGame(Game game) {
        gameRepository.save(game);
    }


}
