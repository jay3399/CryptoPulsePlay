package com.example.cryptopulseplay.domian.game.model;

import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.shared.service.DomainEventPublisher;
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
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game implements Serializable {

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


    private Game(User user ,int amount, Direction predictedDirection) {
        this.timeStamp = LocalDateTime.now();
        this.amount = amount;
        this.direction = predictedDirection;
        setUser(user);
    }

    public void calculateOutcome(Direction actualDirection) {
        if (direction == actualDirection) {
            this.outcome = Outcome.WON;
        } else {
            this.outcome = Outcome.LOST;
        }
        DomainEventPublisher.getInstance().publish(new GameResultEvent(this.id, direction));
    }



    public static Game createGame(User user, int amount, Direction direction) {

        return new Game(user, amount, direction);

    }






}
