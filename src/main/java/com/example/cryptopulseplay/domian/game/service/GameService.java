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

    public Game startGame(User user, int amount, Direction direction) {

        user.playGame(amount);

        return Game.createGame(user, amount, direction);

    }


//    @Transactional
    public void saveGame(Game game) {
        gameRepository.save(game);
    }


}
