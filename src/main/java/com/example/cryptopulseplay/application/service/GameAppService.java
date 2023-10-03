package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.repository.GameRepository;
import com.example.cryptopulseplay.domian.game.service.GameService;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameAppService {

    private final GameService gameService;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Transactional
    public void createGame(Long userId, int amount, Direction direction) {

        User user = userRepository.findById(userId).orElseThrow();

        Game game = gameService.getGame(user, amount, direction);

        gameRepository.save(game);
    }






}
