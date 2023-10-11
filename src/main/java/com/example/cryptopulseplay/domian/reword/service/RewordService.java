package com.example.cryptopulseplay.domian.reword.service;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.repository.GameRepository;
import com.example.cryptopulseplay.domian.reword.model.Reword;
import com.example.cryptopulseplay.domian.reword.model.RewordStatus;
import com.example.cryptopulseplay.domian.reword.repository.RewordRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RewordService {

    private final RewordRepository rewordRepository;
    private final GameRepository gameRepository;


    public List<Reword> findRewordOnPending() {

        return rewordRepository.findAllByRewordStatus(RewordStatus.PENDING);

    }

    @Transactional
    public void createReword(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new EntityNotFoundException("Game Id" + gameId + "not found")
        );
        Reword reword = Reword.create(game);
        rewordRepository.save(reword);
    }


}
