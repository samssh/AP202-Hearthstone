package model.log;

import client.Answer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AnswerLog extends Log {
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

    public AnswerLog() {
    }

    public AnswerLog(Answer answer, String username) {
        super(System.nanoTime(), username);
        type = answer.getClass().getSimpleName();
        if (answer instanceof Answer.LoginAnswer) {
            success = ((Answer.LoginAnswer) answer).isSuccess();
            message = ((Answer.LoginAnswer) answer).getMessage();
        } else if (answer instanceof Answer.ShopDetails) {
            buy = ((Answer.ShopDetails) answer).getBuy().toString();
            sell = ((Answer.ShopDetails) answer).getSell().toString();
            coins = ((Answer.ShopDetails) answer).getCoins();
        } else if (answer instanceof Answer.StatusDetails) {
            bigDeckOverviews = ((Answer.StatusDetails) answer).getBigDeckOverviews().toString();
        } else if (answer instanceof Answer.FirstCollectionDetails) {
            classOfCardNames = ((Answer.FirstCollectionDetails) answer).getClassOfCardNames().toString();
            heroNames = ((Answer.FirstCollectionDetails) answer).getHeroNames().toString();
        } else if (answer instanceof Answer.CollectionDetails) {
            decks = ((Answer.CollectionDetails) answer).getDecks().toString();
            cards = ((Answer.CollectionDetails) answer).getCards().toString();
            if (((Answer.CollectionDetails) answer).getDeckCards() != null)
                deckCards = ((Answer.CollectionDetails) answer).getDeckCards().toString();
            deckName = ((Answer.CollectionDetails) answer).getDeckName();
            canAddDeck = ((Answer.CollectionDetails) answer).isCanAddDeck();
            canChangeHero = ((Answer.CollectionDetails) answer).isCanChangeHero();
        } else if (answer instanceof Answer.showMessage) {
            message = ((Answer.showMessage) answer).getMessage();
        } else if (answer instanceof Answer.GoTo) {
            message = ((Answer.GoTo) answer).getMessage();
            panel = ((Answer.GoTo) answer).getPanel();
        } else if (answer instanceof Answer.PassiveDetails) {
            passives = ((Answer.PassiveDetails) answer).getPassives().toString();
        } else if (answer instanceof Answer.PlayDetails) {
            hand = ((Answer.PlayDetails) answer).getHand().toString();
            ground = ((Answer.PlayDetails) answer).getGround().toString();
            if (((Answer.PlayDetails) answer).getWeapon()!=null)
            weapon = ((Answer.PlayDetails) answer).getWeapon().toString();
            hero = ((Answer.PlayDetails) answer).getHero().toString();
            heroPower = ((Answer.PlayDetails) answer).getHeroPower().toString();
            mana = ((Answer.PlayDetails) answer).getMana();
            deckCards = ((Answer.PlayDetails) answer).getDeckCards() + "";
            eventLog = ((Answer.PlayDetails) answer).getEventLog();
        }
    }
}
