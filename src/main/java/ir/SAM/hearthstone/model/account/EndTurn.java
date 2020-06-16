package ir.SAM.hearthstone.model.account;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class EndTurn extends GameEvent {
    @Column
    @Getter
    @Setter
    private int mana;

    public EndTurn(int mana) {
        this.mana = mana;
    }

    public EndTurn() {
    }

    @Override
    public String toString() {
        return "NextTurn";
    }
}
