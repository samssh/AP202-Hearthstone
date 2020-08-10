package ir.sam.hearthstone.server.model.requests;

import ir.sam.hearthstone.server.util.hibernate.DatabaseDisconnectException;

public interface RequestExecutor {
    default void shutdown() throws DatabaseDisconnectException {
    }

    void login(String username, String password, int mode) throws DatabaseDisconnectException;

    default void logout() throws DatabaseDisconnectException {
    }

    default void deleteAccount() throws DatabaseDisconnectException {
    }

    default void sendShop() throws DatabaseDisconnectException {
    }

    void sellCard(String cardName) throws DatabaseDisconnectException;

    void buyCard(String cardName) throws DatabaseDisconnectException;

    default void sendStatus() throws DatabaseDisconnectException {
    }

    void selectDeck(String deckName) throws DatabaseDisconnectException;

    void sendAllCollectionDetails(String name, String classOfCard, int mana, int lockMode) throws DatabaseDisconnectException;

    void applyCollectionFilter(String name, String classOfCard, int mana, int lockMode) throws DatabaseDisconnectException;

    void newDeck(String deckName, String heroName) throws DatabaseDisconnectException;

    void deleteDeck(String deckName) throws DatabaseDisconnectException;

    void changeDeckName(String oldDeckName, String newDeckName) throws DatabaseDisconnectException;

    void changeHeroDeck(String deckName, String heroName) throws DatabaseDisconnectException;

    void removeCardFromDeck(String cardName, String deckName) throws DatabaseDisconnectException;

    void addCardToDeck(String cardName, String deckName) throws DatabaseDisconnectException;

    void startPlay(String mode) throws DatabaseDisconnectException;

    void selectPlayMode(String modeName);

    void selectPassive(String passiveName) throws DatabaseDisconnectException;

    void selectOpponentDeck(String deckName) throws DatabaseDisconnectException;

    void selectCadOnPassive(int index) throws DatabaseDisconnectException;

    default void confirm() throws DatabaseDisconnectException {
    }

    default void endTurn() throws DatabaseDisconnectException {
    }

    void selectHero(int side) throws DatabaseDisconnectException;

    void selectHeroPower(int side) throws DatabaseDisconnectException;

    void selectMinion(int side, int index, int emptyIndex) throws DatabaseDisconnectException;

    void selectCardInHand(int side, int index) throws DatabaseDisconnectException;

    default void sendGameEvents() throws DatabaseDisconnectException {
    }

    default void exitGame() throws DatabaseDisconnectException {
    }

    default void checkForOpponent() throws DatabaseDisconnectException {
    }

    default void cancelGame() {
    }
}
