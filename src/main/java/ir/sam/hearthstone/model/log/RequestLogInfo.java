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
    private String type, userName, password, cardName, name;
    @Column
    @Getter
    @Setter
    private String classOfCard, deckName, passiveName, heroName, newDeckName;
    @Column
    @Getter
    @Setter
    private int mana, lockMode, mode;

    public RequestLogInfo() {
    }

    public RequestLogInfo(Request request, long id) {
        type = request.getClass().getSimpleName();
        this.id = id;
        request.accept(new Visitor());
    }

    private class Visitor implements RequestLogInfoVisitor {

        @Override
        public void setAddCardToDeck(AddCardToDeck addCardToDeck) {
            deckName = addCardToDeck.getDeckName();
            cardName = addCardToDeck.getCardName();
        }

        @Override
        public void setBuyCard(BuyCard buyCard) {
            cardName = buyCard.getCardName();
        }

        @Override
        public void setChangeDeckName(ChangeDeckName changeDeckName) {
            deckName = changeDeckName.getOldDeckName();
            newDeckName = changeDeckName.getNewDeckName();
        }

        @Override
        public void setChangeHeroDeck(ChangeHeroDeck changeHeroDeck) {
            deckName = changeHeroDeck.getDeckName();
            heroName = changeHeroDeck.getHeroName();
        }

        @Override
        public void setCollectionDetails(CollectionFilter collectionFilter) {
            name = collectionFilter.getName();
            classOfCard = collectionFilter.getClassOfCard();
            mana = collectionFilter.getMana();
            lockMode = collectionFilter.getLockMode();
        }

        @Override
        public void setDeleteAccount(DeleteAccount deleteAccount) {
        }

        @Override
        public void setDeleteDeck(DeleteDeck deleteDeck) {
            deckName = deleteDeck.getDeckName();
        }

        @Override
        public void setEndTurn(EndTurn endTurn) {

        }

        @Override
        public void setExitGame(ExitGame exitGame) {

        }

//        @Override
//        public void setFirstCollection(FirstCollection firstCollection) {
//
//        }

        @Override
        public void setLoginRequest(LoginRequest loginRequest) {
            mode = loginRequest.getMode();
            userName = loginRequest.getUserName();
            password = loginRequest.getPassword();
        }

        @Override
        public void setLogoutRequest(LogoutRequest logoutRequest) {

        }

        @Override
        public void setNewDeck(NewDeck newDeck) {
            deckName = newDeck.getDeckName();
            heroName = newDeck.getHeroName();
        }

        @Override
        public void setPlayCard(PlayCard playCard) {
            cardName = playCard.getCardName();
        }

        @Override
        public void setRemoveCardFromDeck(RemoveCardFromDeck removeCardFromDeck) {
            deckName = removeCardFromDeck.getDeckName();
            cardName = removeCardFromDeck.getCardName();
        }

        @Override
        public void setSelectPassive(SelectPassive selectPassive) {
            passiveName = selectPassive.getPassiveName();
        }

        @Override
        public void setSellCard(SellCard sellCard) {
            cardName = sellCard.getCardName();
        }

        @Override
        public void setShopRequest(ShopRequest shopRequest) {
        }

        @Override
        public void setStartPlaying(StartPlaying startPlaying) {
        }

        @Override
        public void setStatus(StatusRequest statusRequest) {
        }

        @Override
        public void setShutdownRequest(ShutdownRequest shutdownRequest) {
        }
    }
}
