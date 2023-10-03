package com.example.cryptopulseplay.domian.game.model;

import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timeStamp;

    private int amount;

    @Enumerated(value = EnumType.STRING)
    private Direction direction;

    @Enumerated(value = EnumType.STRING)
    private Outcome outcome = Outcome.PENDING;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void setUser(User user) {
        this.user = user;
        user.getGames().add(this);
    }


    public void setGame(int amount, Direction direction) {
        this.timeStamp = LocalDateTime.now();
        this.amount = amount;
        this.direction = direction;
    }

    public static Game createGame(User user, int amount, Direction direction) {

        Game game = new Game();
        game.setGame(amount, direction);
        game.setUser(user);

        return game;

    }






}
