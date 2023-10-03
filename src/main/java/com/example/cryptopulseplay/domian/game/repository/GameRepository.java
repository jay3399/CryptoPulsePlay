package com.example.cryptopulseplay.domian.game.repository;

import com.example.cryptopulseplay.domian.game.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long > {


}
