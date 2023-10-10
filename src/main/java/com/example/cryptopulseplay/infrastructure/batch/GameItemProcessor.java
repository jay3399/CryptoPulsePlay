//package com.example.cryptopulseplay.infrastructure.batch;
//
//import com.example.cryptopulseplay.domian.game.model.Game;
//import com.example.cryptopulseplay.domian.shared.enums.Direction;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class GameItemProcessor implements ItemProcessor<Game, Game> {
//
//
//    private final Direction actualDirection;
//
//    @Override
//    public Game process(Game game) throws Exception {
//        game.calculateOutcome(actualDirection);
//        return game;
//    }
//}
