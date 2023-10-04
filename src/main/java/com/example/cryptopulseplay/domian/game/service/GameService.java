package com.example.cryptopulseplay.domian.game.service;

import com.example.cryptopulseplay.domian.btcprice.service.BtcPriceService;
import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final BtcPriceService btcPriceService;

    public Game startGame(User user, int amount, Direction direction) {

        user.playGame(amount);

        return Game.createGame(user, amount, direction);

    }


    @Scheduled(cron = "59 59 * * * ?")
    public void endGame() {

        double endPrice = btcPriceService.getCurrentPrice().block().getPrice();





    }

}
