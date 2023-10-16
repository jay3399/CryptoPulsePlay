package com.example.cryptopulseplay.domian.reword.service;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.repository.GameRepository;
import com.example.cryptopulseplay.domian.reword.model.Reword;
import com.example.cryptopulseplay.domian.reword.model.RewordStatus;
import com.example.cryptopulseplay.domian.reword.repository.RewordRepository;
import com.example.cryptopulseplay.domian.user.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RewordService {

    private final RewordRepository rewordRepository;


    public List<Reword> findRewordOnPending() {

        return rewordRepository.findAllByRewordStatus(RewordStatus.PENDING);

    }

    @Transactional
    public void createReword(Game game) {

        // 초기화 LazyInitializationException: failed to lazily initialize a collection of role: com.example.cryptopulseplay.domian.user.model.User.games: could not initialize proxy
        User user = game.getUser();
        user.getGames().size();

        Reword reword = Reword.create(game);
        rewordRepository.save(reword);

//        Game game = gameRepository.findById(gameId).orElseThrow(
//                () -> new EntityNotFoundException("Game Id" + gameId + "not found")
//        );

    }


}
