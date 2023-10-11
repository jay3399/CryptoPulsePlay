package com.example.cryptopulseplay.domian.game.model;

import lombok.Getter;

@Getter
public class GameResultEvent {

    private final Long gemaId;
    private final Outcome outcome;
    private final Long userId;


    public GameResultEvent(Long gemaId, Outcome outcome , Long userId) {
        this.gemaId = gemaId;
        this.outcome = outcome;
        this.userId = userId;
    }


}
