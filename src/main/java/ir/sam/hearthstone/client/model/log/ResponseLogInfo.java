package ir.sam.hearthstone.client.model.log;

import ir.sam.hearthstone.client.model.main.BigDeckOverview;
import ir.sam.hearthstone.client.model.main.CardOverview;
import ir.sam.hearthstone.client.model.main.PassiveOverview;
import ir.sam.hearthstone.client.model.main.SmallDeckOverview;
import ir.sam.hearthstone.client.model.response.PlayDetails;
import ir.sam.hearthstone.client.model.response.Response;
import ir.sam.hearthstone.client.model.response.ResponseExecutor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(includeFieldNames = false)
public class ResponseLogInfo {
    @Id
    @Setter
    @Getter
    @EqualsAndHashCode.Include
    private long id;
    @Column
    @Getter
    @Setter
    private long time;
    @Column
    @Getter
    @Setter
    private String type, hand, ground, weapon, hero, heroPower, panel,
            message, deckName, cardName, eventType, deckOverview, cardOverview, manas;
    @Column(length = 200000)
    @Getter
    @Setter
    private String sell, buy, passives, decks, heroNames, classOfCardNames, eventLog,
            cards, bigDeckOverviews, deckCards, events, passiveList;
    @Column
    @Getter
    @Setter
    private boolean success, canAddDeck, canChangeHero, showButton;
    @Column
    @Getter
    @Setter
    private int coins, mana, _index;

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
            setSell(Objects.toString(sell));
            setBuy(Objects.toString(buy));
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
            setBigDeckOverviews(Objects.toString(bigDeckOverviews));
        }

        @Override
        public void setCollectionDetail(List<CardOverview> cards, List<SmallDeckOverview> decks, List<CardOverview> deckCards
                , boolean canAddDeck, boolean canChangeHero, String deckName, List<String> heroNames, List<String> classOfCardNames) {
            setCards(Objects.toString(cards));
            setDecks(Objects.toString(decks));
            setDeckCards(Objects.toString(deckCards));
            setCanAddDeck(canAddDeck);
            setCanChangeHero(canChangeHero);
            setDeckName(deckName);
            setHeroNames(Objects.toString(heroNames));
            setClassOfCardNames(Objects.toString(classOfCardNames));
        }

        @Override
        public void putCollectionDeckEvent(String type, String deckName, SmallDeckOverview newDeck) {
            setEventType(type);
            setDeckName(deckName);
            setDeckOverview(Objects.toString(newDeck));
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
            setPassiveList(Objects.toString(passives));
            setDecks(Objects.toString(decks));
            setCards(Objects.toString(cards));
            setMessage(message);
            setShowButton(showButton);
        }

        @Override
        public void changeCardOnPassive(CardOverview cardOverview, int index) {
            setCardOverview(Objects.toString(cardOverview));
            set_index(index);
        }

        @Override
        public void setPlayDetail(List<PlayDetails.Event> events, String eventLog, int[] mana, long time) {
            setEvents(Objects.toString(events));
            setEventLog(eventLog);
            setManas(Arrays.toString(mana));
            setTime(time);
        }
    }
}
