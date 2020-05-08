package model.account;

import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public abstract class GameEvent implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @Getter
    @Setter
    private GameHistory gameHistory;


    public GameEvent() {
    }

    public GameEvent(GameHistory gameHistory) {
        this.gameHistory = gameHistory;
    }
}
