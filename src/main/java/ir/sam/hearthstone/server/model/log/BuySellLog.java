package ir.sam.hearthstone.server.model.log;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BuySellLog extends Log {
    @Column(name = "last_coin")
    @Getter
    @Setter
    private int lastCoin;
    @Column(name = "new_coin")
    @Getter
    @Setter
    private int newCoin;
    @Column(name = "card_name")
    @Getter
    @Setter
    private String cardName;
    @Column
    @Getter
    @Setter
    private String type;

    public BuySellLog() {
    }

    public BuySellLog(String username, int lastCoin, int newCoin, String cardName, String type) {
        super(System.currentTimeMillis(), username);
        this.lastCoin = lastCoin;
        this.newCoin = newCoin;
        this.cardName = cardName;
        this.type = type;
    }
}
