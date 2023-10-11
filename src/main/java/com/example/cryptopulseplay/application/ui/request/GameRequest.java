package com.example.cryptopulseplay.application.ui.request;

import com.example.cryptopulseplay.domian.shared.enums.Direction;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRequest {


    @Min(value = 10 , message = "amount must be greater than 10")
    private int amount;
    private Direction direction;


}

