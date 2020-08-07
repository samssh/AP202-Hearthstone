package ir.sam.hearthstone.server.model.log;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CollectionLog extends Log {
    @Column(name = "card_name")
    @Setter
    @Getter
    private String cardName;
    @Column(name = "hero_name")
    @Setter
    @Getter
    private String heroName;
    @Column(name = "deck_name")
    @Setter
    @Getter
    private String deckName;
    @Column
    @Setter
    @Getter
    private String type;
    @Column(name = "new_deck_name")
    @Setter
    @Getter
    private String newDeckName;

    public CollectionLog(String username, String cardName
            , String heroName, String deckName, String type, String newDeckName) {
        super(System.currentTimeMillis(), username);
        this.cardName = cardName;
        this.heroName = heroName;
        this.deckName = deckName;
        this.type = type;
        this.newDeckName = newDeckName;
    }

    public CollectionLog() {
    }
}
