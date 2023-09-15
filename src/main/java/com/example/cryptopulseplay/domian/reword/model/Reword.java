package com.example.cryptopulseplay.domian.reword.model;

import com.example.cryptopulseplay.domian.game.model.Game;
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

@Entity
public class Reword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int amount;
    @Enumerated(EnumType.STRING)
    private RewordStatus rewordStatus;


    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "gameId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;


}
