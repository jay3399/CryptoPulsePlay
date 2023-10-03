package com.example.cryptopulseplay.domian.game.service;

import com.example.cryptopulseplay.application.ui.request.GameRequest;
import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.repository.GameRepository;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    public Game getGame(User user, int amount, Direction direction) {

        user.playGame(amount);

        return Game.createGame(user, amount, direction);

    }

}
