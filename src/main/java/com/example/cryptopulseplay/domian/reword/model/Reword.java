package com.example.cryptopulseplay.domian.reword.model;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.game.model.Outcome;
import com.example.cryptopulseplay.domian.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Reword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int amount;
    @Enumerated(EnumType.STRING)
    private RewordStatus rewordStatus = RewordStatus.PENDING;


    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "gameId")
    @OneToOne(fetch = FetchType.LAZY , cascade =  CascadeType.PERSIST)
    private Game game;


    private Reword(Game game) {
        this.game = game;
        this.user = game.getUser();
        calculateAmount(game.getOutcome(), game.getAmount());
    }

    private void calculateAmount(Outcome outcome, int amount) {

        if (outcome == Outcome.WON) {
            this.amount = amount * 2;
        } else {
            this.amount = -amount;
        }
    }

    public void applyReword() {
        try {
            this.user.updatePoints(amount);
            this.rewordStatus = RewordStatus.PAID;
            System.out.println(this.rewordStatus);
        } catch (Exception e) {
            this.rewordStatus = RewordStatus.REJECTED;
            // 실패시 예외추가.
            return;
        }


    }


    public static Reword create(Game game) {

        return new Reword(game);


//        아래는 reword 의 상태를 변경한다. 상태를 변경하지말고 새로운 인스턴스를 반환하고 외부에서 생성자생성을 막는다.

//        Reword reword = new Reword();
//
//        reword.set(game, outcome);
//
//        return reword;


    }


}
