package ir.sam.hearthstone.model.log;

import ir.sam.hearthstone.requests.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode(of = "id")
public class RequestLogInfo {
    @Id
    @Getter
    @Setter
    private long id;
    @Column
    @Getter
    @Setter
    private String type, userName, password, cardName, name, modeName;
    @Column
    @Getter
    @Setter
    private String classOfCard, deckName, passiveName, heroName, newDeckName;
    @Column
    @Getter
    @Setter
    private int mana, lockMode, mode, side, index, emptyIndex;

    public RequestLogInfo() {
    }

    public RequestLogInfo(Request request, long id) {
        type = request.getClass().getSimpleName();
        this.id = id;
        request.execute(new SetDetails());
    }

    private class SetDetails implements RequestExecutor {

        @Override
        public void login(String username, String password, int mode) {
            setUserName(username);
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
