package ir.sam.hearthstone.server.model.requests;

public interface RequestExecutor {
    default void shutdown() {
    }

    void login(String username, String password, int mode);

    default void logout() {
    }

    default void deleteAccount() {
    }

    default void sendShop() {
    }

    void sellCard(String cardName);

    void buyCard(String cardName);

    default void sendStatus() {
    }

    void selectDeck(String deckName);

    void sendAllCollectionDetails(String name, String classOfCard, int mana, int lockMode);

    void applyCollectionFilter(String name, String classOfCard, int mana, int lockMode);

    void newDeck(String deckName, String heroName);

    void deleteDeck(String deckName);

    void changeDeckName(String oldDeckName, String newDeckName);

    void changeHeroDeck(String deckName, String heroName);

    void removeCardFromDeck(String cardName, String deckName);

    void addCardToDeck(String cardName, String deckName);

    void startPlay(String mode);

    void selectPlayMode(String modeName);

    void selectPassive(String passiveName);

    void selectOpponentDeck(String deckName);

    void selectCadOnPassive(int index);

    default void confirm() {
    }

    default void endTurn() {
    }

    void selectHero(int side);

    void selectHeroPower(int side);

    void selectMinion(int side, int index, int emptyIndex);

    void selectCardInHand(int side, int index);

    default void exitGame() {
    }
}
