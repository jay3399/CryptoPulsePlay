//package com.example.cryptopulseplay.infrastructure.batch;
//
//import com.example.cryptopulseplay.domian.game.model.Game;
//import com.example.cryptopulseplay.domian.game.repository.GameRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.item.Chunk;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class GameItemWriter implements ItemWriter<Game> {
//
//    private final GameRepository gameRepository;
//    @Override
//    public void write(Chunk<? extends Game> games) throws Exception {
//        gameRepository.saveAll(games);
//    }
//}
