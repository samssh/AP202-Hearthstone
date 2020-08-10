package ir.sam.hearthstone.server.model.log;

import ir.sam.hearthstone.server.model.requests.Request;
import ir.sam.hearthstone.server.model.requests.RequestExecutor;
import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;
import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "request_log_info", schema = "log")
public class RequestLogInfo implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;
    @Column
    @Getter
    @Setter
    private String type;
    @Column
    @Getter
    @Setter
    private String username;
    @Column
    @Getter
    @Setter
    private String password;
    @Column(name = "card_name")
    @Getter
    @Setter
    private String cardName;
    @Column
    @Getter
    @Setter
    private String name;
    @Column(name = "mode_name")
    @Getter
    @Setter
    private String modeName;
    @Column(name = "class_of_card")
    @Getter
    @Setter
    private String classOfCard;
    @Column(name = "deck_name")
    @Getter
    @Setter
    private String deckName;
    @Column(name = "passive_name")
    @Getter
    @Setter
    private String passiveName;
    @Column(name = "hero_name")
    @Getter
    @Setter
    private String heroName;
    @Column(name = "new_deck_name")
    @Getter
    @Setter
    private String newDeckName;
    @Column
    @Getter
    @Setter
    private int mana;
    @Column(name = "lock_mode")
    @Getter
    @Setter
    private int lockMode;
    @Column
    @Getter
    @Setter
    private int mode;
    @Column
    @Getter
    @Setter
    private int side;
    @Column(name = "_index")
    @Getter
    @Setter
    private int index;
    @Column(name = "empty_index")
    @Getter
    @Setter
    private int emptyIndex;

    public RequestLogInfo() {
    }

    public RequestLogInfo(Request request) {
        type = request.getClass().getSimpleName();
        try {
            request.execute(new SetDetails());
        } catch (DatabaseDisconnectException ignore) {
        }
    }

    @Override
    public String toString() {
        return "RequestLogInfo{" +
                "id=" + id + ", type='" + type + '\'' + ", username='" + username + '\'' +
                ", password='" + password + '\'' + ", cardName='" + cardName + '\'' + ", name='" + name + '\'' +
                ", modeName='" + modeName + '\'' + ", classOfCard='" + classOfCard + '\'' +
                ", deckName='" + deckName + '\'' + ", passiveName='" + passiveName + '\'' +
                ", heroName='" + heroName + '\'' + ", newDeckName='" + newDeckName + '\'' + ", mana=" + mana +
                ", lockMode=" + lockMode + ", mode=" + mode + ", side=" + side + ", index=" + index +
                ", emptyIndex=" + emptyIndex + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestLogInfo that = (RequestLogInfo) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private class SetDetails implements RequestExecutor {

        @Override
        public void login(String username, String password, int mode) {
            setUsername(username);
            setPassword(password);
            setMode(mode);
        }

        @Override
        public void sellCard(String cardName) {
            setCardName(cardName);
        }

        @Override
        public void buyCard(String cardName) {
            setCardName(cardName);
        }

        @Override
        public void selectDeck(String deckName) {
            setDeckName(deckName);
        }

        @Override
        public void sendAllCollectionDetails(String name, String classOfCard, int mana, int lockMode) {
            setName(name);
            setClassOfCard(classOfCard);
            setMana(mana);
            setLockMode(lockMode);
        }

        @Override
        public void applyCollectionFilter(String name, String classOfCard, int mana, int lockMode) {
            setName(name);
            setClassOfCard(classOfCard);
            setMana(mana);
            setLockMode(lockMode);
        }

        @Override
        public void newDeck(String deckName, String heroName) {
            setDeckName(deckName);
            setHeroName(heroName);
        }

        @Override
        public void deleteDeck(String deckName) {
            setDeckName(deckName);
        }

        @Override
        public void changeDeckName(String oldDeckName, String newDeckName) {
            setDeckName(oldDeckName);
            setNewDeckName(newDeckName);
        }

        @Override
        public void changeHeroDeck(String deckName, String heroName) {
            setDeckName(deckName);
            setHeroName(heroName);
        }

        @Override
        public void removeCardFromDeck(String cardName, String deckName) {
            setCardName(cardName);
            setDeckName(deckName);
        }

        @Override
        public void addCardToDeck(String cardName, String deckName) {
            setCardName(cardName);
            setDeckName(deckName);
        }

        @Override
        public void startPlay(String mode) {
            setModeName(mode);
        }

        @Override
        public void selectPlayMode(String modeName) {
            setModeName(modeName);
        }

        @Override
        public void selectPassive(String passiveName) {
            setPassiveName(passiveName);
        }

        @Override
        public void selectOpponentDeck(String deckName) {
            setDeckName(deckName);
        }

        @Override
        public void selectCadOnPassive(int index) {
            setIndex(index);
        }

        @Override
        public void selectHero(int side) {
            setSide(side);
        }

        @Override
        public void selectHeroPower(int side) {
            setSide(side);
        }

        @Override
        public void selectMinion(int side, int index, int emptyIndex) {
            setSide(side);
            setIndex(index);
            setEmptyIndex(emptyIndex);
        }

        @Override
        public void selectCardInHand(int side, int index) {
            setSide(side);
            setIndex(index);
        }
    }
}
