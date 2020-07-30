package ir.sam.hearthstone.model.log;

import ir.sam.hearthstone.response.*;
import ir.sam.hearthstone.view.model.BigDeckOverview;
import ir.sam.hearthstone.view.model.CardOverview;
import ir.sam.hearthstone.view.model.PassiveOverview;
import ir.sam.hearthstone.view.model.SmallDeckOverview;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ir.sam.hearthstone.response.ResponseExecutor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;

@Entity
@EqualsAndHashCode(of = "id")
public class ResponseLogInfo {
    @Id
    @Setter
    @Getter
    private long id;
    @Column
    @Getter
    @Setter
    private long time;
    @Column
    @Getter
    @Setter
    private String type, hand, ground, weapon, hero, heroPower, eventLog, panel,
            message, deckName, cardName, eventType, deckOverview, cardOverview, manas;
    @Column(length = 200000)
    @Getter
    @Setter
    private String sell, buy, passives, decks, heroNames, classOfCardNames,
            cards, bigDeckOverviews, deckCards, events, passiveList;
    @Column
    @Getter
    @Setter
    private boolean success, canAddDeck, canChangeHero, showButton;
    @Column
    @Getter
    @Setter
    private int coins, mana, index;

    public ResponseLogInfo() {
    }

    public ResponseLogInfo(Response response, long id) {
        this.id = id;
        type = response.getClass().getSimpleName();
        response.execute(new SetDetails());
    }


    private class SetDetails implements ResponseExecutor {
        @Override
        public void login(boolean success, String message) {
            setSuccess(success);
            setMessage(message);
        }

        @Override
        public void setShopDetails(List<CardOverview> sell, List<CardOverview> buy, int coin) {
            setSell(sell.toString());
            setBuy(buy.toString());
            setCoins(coin);
        }

        @Override
        public void putShopEvent(String cardName, String type, int coins) {
            setCardName(cardName);
            setEventType(type);
            setCoins(coins);
        }

        @Override
        public void setStatusDetails(List<BigDeckOverview> bigDeckOverviews) {
            setBigDeckOverviews(bigDeckOverviews.toString());
        }

        @Override
        public void setCollectionDetail(List<CardOverview> cards, List<SmallDeckOverview> decks, List<CardOverview> deckCards
                , boolean canAddDeck, boolean canChangeHero, String deckName, List<String> heroNames, List<String> classOfCardNames) {
            setCards(cards.toString());
            setDecks(decks.toString());
            setDeckCards(deckCards.toString());
            setCanAddDeck(canAddDeck);
            setCanChangeHero(canChangeHero);
            setDeckName(deckName);
            setHeroNames(heroNames.toString());
            setClassOfCardNames(classOfCardNames.toString());
        }

        @Override
        public void putCollectionDeckEvent(String type, String deckName, SmallDeckOverview newDeck) {
            setEventType(type);
            setDeckName(deckName);
            setDeckOverview(newDeck.toString());
        }

        @Override
        public void putCollectionCardEvent(String type, String cardName, boolean canAddDeck, boolean canChangeHero) {
            setEventType(type);
            setCardName(cardName);
            setCanAddDeck(canAddDeck);
            setCanChangeHero(canChangeHero);
        }

        @Override
        public void showMessage(String message) {
            setMessage(message);
        }

        @Override
        public void goTo(String panel, String message) {
            setPanel(panel);
            setMessage(message);
        }

        @Override
        public void setPassives(List<PassiveOverview> passives, List<SmallDeckOverview> decks, List<CardOverview> cards, String message, boolean showButton) {
            setPassiveList(passives.toString());
            setDecks(decks.toString());
            setCards(cards.toString());
            setMessage(message);
            setShowButton(showButton);
        }

        @Override
        public void changeCardOnPassive(CardOverview cardOverview, int index) {
            setCardOverview(cardOverview.toString());
            setIndex(index);
        }

        @Override
        public void setPlayDetail(List<PlayDetails.Event> events, String eventLog, int[] mana, long time) {
            setEvents(events.toString());
            setEventLog(eventLog);
            setManas(Arrays.toString(mana));
            setTime(time);
        }
    }
}
