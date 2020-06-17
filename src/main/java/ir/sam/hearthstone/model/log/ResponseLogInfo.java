package ir.sam.hearthstone.model.log;

import ir.sam.hearthstone.response.*;
import ir.sam.hearthstone.util.Visitable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ir.sam.hearthstone.response.ResponseLogInfoVisitor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
    private String passives, hand, ground, weapon, hero, heroPower, eventLog;
    @Column
    @Getter
    @Setter
    private String sell, buy, bigDeckOverviews, decks, deckCards, deckName;
    @Column
    @Getter
    @Setter
    private String type, message, heroNames, classOfCardNames, cards, panel;
    @Column
    @Getter
    @Setter
    private boolean success, canAddDeck, canChangeHero;
    @Column
    @Getter
    @Setter
    private int coins, mana;

    public ResponseLogInfo() {
    }

    public ResponseLogInfo(Visitable<ResponseLogInfoVisitor> response, long id) {
        this.id = id;
        type = response.getClass().getSimpleName();
        response.accept(new Visitor());
    }


    private class Visitor implements ResponseLogInfoVisitor{

        @Override
        public void setPassiveDetailsInfo(PassiveDetails response) {
            passives = response.getPassives().toString();

        }

        @Override
        public void setPlayDetailsInfo(PlayDetails playDetails) {
            hand = playDetails.getHand().toString();
            ground = playDetails.getGround().toString();
            if (playDetails.getWeapon() != null)
                weapon = playDetails.getWeapon().toString();
            hero = playDetails.getHero().toString();
            heroPower = playDetails.getHeroPower().toString();
            mana = playDetails.getMana();
            deckCards = playDetails.getDeckCards() + "";
            eventLog = playDetails.getEventLog();
        }

        @Override
        public void setGoToInfo(GoTo goTo) {
            message = goTo.getMessage();
            panel = goTo.getPanel();
        }

        @Override
        public void setShowMessageInfo(ShowMessage showMessage) {
            message = showMessage.getMessage();
        }

        @Override
        public void setCollectionDetailsInfo(CollectionDetails collectionDetails) {
            decks = collectionDetails.getDecks().toString();
            cards = collectionDetails.getCards().toString();
            if (collectionDetails.getDeckCards() != null)
                deckCards = collectionDetails.getDeckCards().toString();
            deckName = collectionDetails.getDeckName();
            canAddDeck = collectionDetails.isCanAddDeck();
            canChangeHero = collectionDetails.isCanChangeHero();
        }

        @Override
        public void setFirstCollectionDetailsInfo(FirstCollectionDetails firstCollectionDetails) {
            classOfCardNames = firstCollectionDetails.getClassOfCardNames().toString();
            heroNames = firstCollectionDetails.getHeroNames().toString();
        }

        @Override
        public void setStatusDetailsInfo(StatusDetails statusDetails) {
            bigDeckOverviews = statusDetails.getBigDeckOverviews().toString();
        }

        @Override
        public void setShopDetailsInfo(ShopDetails shopDetails) {
            buy = shopDetails.getBuy().toString();
            sell = shopDetails.getSell().toString();
            coins = shopDetails.getCoins();
        }

        @Override
        public void setLoginResponseInfo(LoginResponse loginResponse) {
            success = loginResponse.isSuccess();
            message = loginResponse.getMessage();
        }
    }
}
