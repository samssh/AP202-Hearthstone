package ir.sam.hearthstone.model.log;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class InGameLog extends Log{
    @Column
    @Getter
    @Setter
    private String cardName,type;
    @Column
    @Getter
    @Setter
    private int mana;

    public InGameLog(String username, String cardName, String type, int mana) {
        super(System.currentTimeMillis(), username);
        this.cardName = cardName;
        this.type = type;
        this.mana = mana;
    }

    public InGameLog(String username, String cardName, String type) {
        super(System.currentTimeMillis(), username);
        this.cardName = cardName;
        this.type = type;
    }

    public InGameLog() {
    }
}
