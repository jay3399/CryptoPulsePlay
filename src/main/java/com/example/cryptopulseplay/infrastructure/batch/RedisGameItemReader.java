//package com.example.cryptopulseplay.infrastructure.batch;
//
//import com.example.cryptopulseplay.domian.game.model.Game;
//import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
//import java.util.Collections;
//import java.util.Iterator;
//import java.util.Set;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.NonTransientResourceException;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.UnexpectedInputException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//@StepScope
//public class RedisGameItemReader implements ItemReader<Game> {
//
//    private final RedisUtil redisUtil;
//
//    private Iterator<String> gameKEysIterator;
//
//    @Autowired
//    public RedisGameItemReader(RedisUtil redisUtil) {
//        this.redisUtil = redisUtil;
//        Set<String> gameKeys = redisUtil.gameKeys();
////        if (gameKeys != null) {
////            this.gameKEysIterator = gameKeys.iterator();
////        }
//        this.gameKEysIterator =
//                (gameKeys != null) ? gameKeys.iterator() : Collections.emptyIterator();
//    }
//
//    @Override
//    public synchronized Game read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
//
//        if (gameKEysIterator.hasNext()) {
//            String gameKey = gameKEysIterator.next();
//            Game game = redisUtil.getGame(gameKey);
//            if (game == null) {
//                throw new UnexpectedInputException("Not found for Key:" + gameKey);
//            }
//            return game;
//        }
//        return null;
//    }
//}
