package com.example.cryptopulseplay.application.ui.request;

import com.example.cryptopulseplay.domian.shared.enums.Direction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRequest {


    private Long userId;
    private int amount;
    private Direction direction;

}

