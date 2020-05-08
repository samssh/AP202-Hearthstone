package model.log;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CollectionLog extends Log {
    @Column
    @Setter
    @Getter
    private String cardName, heroName, deckName, type, newDeckName;

    public CollectionLog(String username, String cardName
            , String heroName, String deckName, String type, String newDeckName) {
        super(System.nanoTime(), username);
        this.cardName = cardName;
        this.heroName = heroName;
        this.deckName = deckName;
        this.type = type;
        this.newDeckName = newDeckName;
    }

    public CollectionLog() {
    }
}
