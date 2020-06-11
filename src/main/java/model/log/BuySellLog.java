package model.log;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BuySellLog extends Log{
    @Column
    @Getter
    @Setter
    private int lastCoin,newCoin;
    @Column
    @Getter
    @Setter
    private String cardName,type;

    public BuySellLog() {
    }

    public BuySellLog(String username, int lastCoin, int newCoin, String cardName,String type) {
        super(System.currentTimeMillis(), username);
        this.lastCoin = lastCoin;
        this.newCoin = newCoin;
        this.cardName = cardName;
        this.type = type;
    }
}
