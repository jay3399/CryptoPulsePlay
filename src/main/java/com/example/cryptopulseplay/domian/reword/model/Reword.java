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


    // 게임 객체가 초기화도지않은 user 를 넣고 , 다시 그 유저에 add this 를 함  ,
    // this.user.getGames().add(game);
    private Reword(Game game) {
        this.game = game;
        this.user = game.getUser();
        calculateAmountV2();
    }

    private void calculateAmountV2() {

        if (this.game.getOutcome() == Outcome.WON) {
            this.amount = this.game.getAmount() * 2;
        } else {
            this.amount = -this.game.getAmount();
        }
    }




    public void applyReword() {
        try {
            this.user.updatePoints(amount);
            this.rewordStatus = RewordStatus.PAID;
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
