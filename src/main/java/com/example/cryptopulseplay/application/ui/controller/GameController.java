package com.example.cryptopulseplay.application.ui.controller;

import com.example.cryptopulseplay.application.service.GameAppService;
import com.example.cryptopulseplay.application.ui.request.GameRequest;
import com.example.cryptopulseplay.application.ui.response.GameResponse;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameAppService gameAppService;


    @PostMapping("/game")
    public CompletableFuture<ResponseEntity<GameResponse>> createGame(@RequestBody GameRequest gameRequest) {

        return gameAppService.createGame(gameRequest.getUserId(), gameRequest.getAmount() , gameRequest.getDirection())
                .thenApply((game) -> ResponseEntity.ok().body(new GameResponse("Game crated")))
                .exceptionally(e -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GameResponse("Creating game is failed")));
    }


}




//
//        gameAppService.createGame(gameRequest.getUserId() , gameRequest.getAmount(), gameRequest.getDirection());
//
//        return ResponseEntity.ok().build();
