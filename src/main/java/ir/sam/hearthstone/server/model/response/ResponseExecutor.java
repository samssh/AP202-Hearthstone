package ir.sam.hearthstone.server.model.response;

import ir.sam.hearthstone.server.model.client.BigDeckOverview;
import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.client.PassiveOverview;
import ir.sam.hearthstone.server.model.client.SmallDeckOverview;

import java.util.List;

public interface ResponseExecutor {
    void login(boolean success, String message);

    void setShopDetails(List<CardOverview> sell, List<CardOverview> buy, int coin);

    void putShopEvent(String cardName, String type, int coins);

    void setStatusDetails(List<BigDeckOverview> bigDeckOverviews);

    void setCollectionDetail(List<CardOverview> cards, List<SmallDeckOverview> decks,
                             List<CardOverview> deckCards, boolean canAddDeck,
                             boolean canChangeHero, String deckName,
                             List<String> heroNames, List<String> classOfCardNames);

    void putCollectionDeckEvent(String type, String deckName, SmallDeckOverview newDeck);

    void putCollectionCardEvent(String type, String cardName, boolean canAddDeck
            , boolean canChangeHero);

    void showMessage(String message);

    void goTo(String panel, String message);

    void setPassives(List<PassiveOverview> passives, List<SmallDeckOverview> decks
            , List<CardOverview> cards, String message, boolean showButton);

    void changeCardOnPassive(CardOverview cardOverview, int index);

    void setPlayDetail(List<PlayDetails.Event> events, String eventLog
            , int[] mana, double time);

    default void doLogout() {
    }

    default void doShutDown() {
    }

    void gameNames(List<String> names);
}