package model.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class EndGame extends GameEvent{
    @Column
    @Enumerated (value = EnumType.STRING)
    @Getter
    @Setter
    private EndGameType type;


    public EndGame() {
    }

    public EndGame(EndGameType type,GameHistory gameHistory) {
        super(gameHistory);
        this.type = type;
    }

    public enum EndGameType{
        WIN,LOSE;
    }

    @Override
    public String toString() {
        return "EndGame(" +
                "type=" + type +
                ')';
    }
}
