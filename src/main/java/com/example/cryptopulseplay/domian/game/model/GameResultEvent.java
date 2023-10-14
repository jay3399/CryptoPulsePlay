package com.example.cryptopulseplay.domian.game.model;

import lombok.Getter;

@Getter
public class GameResultEvent {

    private final Game game;
    private final Outcome outcome;
    private final Long userId;


    public GameResultEvent(Game game, Outcome outcome , Long userId) {
        this.game = game;
        this.outcome = outcome;
        this.userId = userId;
    }


}
