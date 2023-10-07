package com.example.cryptopulseplay.domian.game.model;

import com.example.cryptopulseplay.domian.shared.enums.Direction;

public class GameResultEvent {

    private final Long gemaId;
    private final Direction direction;


    public GameResultEvent(Long gemaId, Direction direction) {
        this.gemaId = gemaId;
        this.direction = direction;
    }


}
