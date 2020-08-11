package ir.sam.hearthstone.client.model.log;

import ir.sam.hearthstone.client.model.main.BigDeckOverview;
import ir.sam.hearthstone.client.model.main.CardOverview;
import ir.sam.hearthstone.client.model.main.PassiveOverview;
import ir.sam.hearthstone.client.model.main.SmallDeckOverview;
import ir.sam.hearthstone.client.model.response.PlayDetails;
import ir.sam.hearthstone.client.model.response.Response;
import ir.sam.hearthstone.client.model.response.ResponseExecutor;
import ir.sam.hearthstone.client.util.hibernate.SaveAble;
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

@Entity(name = "response_log_info")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(includeFieldNames = false)
public class ResponseLogInfo implements SaveAble {
    @Id
    @Setter
    @Getter
    @EqualsAndHashCode.Include
    private long id;
    @Column
    @Getter
    @Setter
    private double time;
    @Column
    @Getter
    @Setter
    private String type;
    @Column
    @Getter
    @Setter
    private String hand;
    @Column
    @Getter
    @Setter
    private String ground;
    @Column
    @Getter
    @Setter
    private String weapon;
    @Column
    @Getter
    @Setter
    private String hero;
    @Column(name = "hero_power")
    @Getter
    @Setter
    private String heroPower;
    @Column
    @Getter
    @Setter
    private String panel;
    @Column
    @Getter
    @Setter
    private String message;
    @Column(name = "deck_name")
    @Getter
    @Setter
    private String deckName;
    @Column(name = "card_name")
    @Getter
    @Setter
    private String cardName;
    @Column(name = "event_type")
    @Getter
    @Setter
    private String eventType;
    @Column(name = "deck_overview")
    @Getter
    @Setter
    private String deckOverview;
    @Column(name = "card_overview")
    @Getter
    @Setter
    private String cardOverview;
    @Column
    @Getter
    @Setter
    private String manas;
    @Column(length = 200000)
    @Getter
    @Setter
    private String sell;
    @Column(length = 200000)
    @Getter
    @Setter
    private String buy;
    @Column(length = 200000)
    @Getter
    @Setter
    private String passives;
    @Column(length = 200000)
    @Getter
    @Setter
    private String decks;
    @Column(name = "hero_name", length = 200000)
    @Getter
    @Setter
    private String heroNames;
    @Column(name = "class_of_card_names", length = 200000)
    @Getter
    @Setter
    private String classOfCardNames;
    @Column(name = "event_log", length = 200000)
    @Getter
    @Setter
    private String eventLog;
    @Column(length = 200000)
    @Getter
    @Setter
    private String cards;
    @Column(name = "big_deck_overviews", length = 200000)
    @Getter
    @Setter
    private String bigDeckOverviews;
    @Column(name = "deck_cards", length = 200000)
    @Getter
    @Setter
    private String deckCards;
    @Column(length = 200000)
    @Getter
    @Setter
    private String events;
    @Column(name = "passive_list", length = 200000)
    @Getter
    @Setter
    private String passiveList;
    @Column
    @Getter
    @Setter
    private boolean success;
    @Column(name = "can_add_deck")
    @Getter
    @Setter
    private boolean canAddDeck;
    @Column(name = "can_change_hero")
    @Getter
    @Setter
    private boolean canChangeHero;
    @Column(name = "show_button")
    @Getter
    @Setter
    private boolean showButton;
    @Column
    @Getter
    @Setter
    private int coins;
    @Column
    @Getter
    @Setter
    private int mana;
    @Column(name = "_index")
    @Getter
    @Setter
    private int index;

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
            setIndex(index);
        }

        @Override
        public void setPlayDetail(List<PlayDetails.Event> events, String eventLog, int[] mana, double time) {
            setEvents(Objects.toString(events));
            setEventLog(eventLog);
            setManas(Arrays.toString(mana));
            setTime(time);
        }

        @Override
        public void showGameNames(List<String> names) {
            setEvents(names.toString());
        }
    }
}
