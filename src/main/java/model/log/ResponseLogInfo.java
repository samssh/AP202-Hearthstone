package model.log;

import lombok.EqualsAndHashCode;
import response.*;
import lombok.Getter;
import lombok.Setter;

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

    public ResponseLogInfo(Response response,long id) {
        this.id = id;
        type = response.getClass().getSimpleName();
        if (response instanceof LoginResponse) {
            success = ((LoginResponse) response).isSuccess();
            message = ((LoginResponse) response).getMessage();
        } else if (response instanceof ShopDetails) {
            buy = ((ShopDetails) response).getBuy().toString();
            sell = ((ShopDetails) response).getSell().toString();
            coins = ((ShopDetails) response).getCoins();
        } else if (response instanceof StatusDetails) {
            bigDeckOverviews = ((StatusDetails) response).getBigDeckOverviews().toString();
        } else if (response instanceof FirstCollectionDetails) {
            classOfCardNames = ((FirstCollectionDetails) response).getClassOfCardNames().toString();
            heroNames = ((FirstCollectionDetails) response).getHeroNames().toString();
        } else if (response instanceof CollectionDetails) {
            decks = ((CollectionDetails) response).getDecks().toString();
            cards = ((CollectionDetails) response).getCards().toString();
            if (((CollectionDetails) response).getDeckCards() != null)
                deckCards = ((CollectionDetails) response).getDeckCards().toString();
            deckName = ((CollectionDetails) response).getDeckName();
            canAddDeck = ((CollectionDetails) response).isCanAddDeck();
            canChangeHero = ((CollectionDetails) response).isCanChangeHero();
        } else if (response instanceof showMessage) {
            message = ((showMessage) response).getMessage();
        } else if (response instanceof GoTo) {
            message = ((GoTo) response).getMessage();
            panel = ((GoTo) response).getPanel();
        } else if (response instanceof PassiveDetails) {
            passives = ((PassiveDetails) response).getPassives().toString();
        } else if (response instanceof PlayDetails) {
            hand = ((PlayDetails) response).getHand().toString();
            ground = ((PlayDetails) response).getGround().toString();
            if (((PlayDetails) response).getWeapon()!=null)
            weapon = ((PlayDetails) response).getWeapon().toString();
            hero = ((PlayDetails) response).getHero().toString();
            heroPower = ((PlayDetails) response).getHeroPower().toString();
            mana = ((PlayDetails) response).getMana();
            deckCards = ((PlayDetails) response).getDeckCards() + "";
            eventLog = ((PlayDetails) response).getEventLog();
        }
    }
}
