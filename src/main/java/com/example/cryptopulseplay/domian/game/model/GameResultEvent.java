package com.example.cryptopulseplay.domian.game.model;

import com.example.cryptopulseplay.domian.shared.enums.Direction;
import lombok.Getter;

@Getter
public class GameResultEvent {

    private final Long gemaId;
    private final Outcome outcome;


    public GameResultEvent(Long gemaId, Outcome outcome) {
        this.gemaId = gemaId;
        this.outcome = outcome;
    }


}
