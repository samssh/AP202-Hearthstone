package server;

import lombok.Getter;
import model.account.*;
import model.main.*;
import sun.awt.geom.AreaOp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static server.Server.MAX_MANA;
import static server.Server.STARTING_HAND_CARDS;

class Game {
    final int manaPerTurn = 1;
    final int cardPerTurn = 1;
    @Getter
    private final List<Minion> ground;
    @Getter
    private final List<Card> handCard, deck;
    private final GameHistory gameHistory;
    @Getter
    private final Hero hero;
    @Getter
    private Weapon activeWeapon;
    @Getter
    private int mana, nextMana;

    Game(Deck deck, Passive passive) {
        this.hero = deck.getHero();
        this.ground = new ArrayList<>();
        this.deck = deckToList(deck);
        this.gameHistory = new GameHistory(passive, hero, new ArrayList<>(this.deck));
        this.handCard = new ArrayList<>();
        for (int i = 0; i < STARTING_HAND_CARDS; i++) {
            drawCard();
        }
        this.mana = 1;
        this.nextMana = 2;
    }

    private List<Card> deckToList(Deck d) {
        List<Card> result = new ArrayList<>();
        Map<Card, CardDetails> map = d.getCards();
        for (Card c : map.keySet()) {
            for (int i = 0; i < map.get(c).getRepeatedTimes(); i++) {
                result.add(c);
            }
        }
        return result;
    }

    void endTurn() {
        gameHistory.getEvents().add(new EndTurn(nextMana));
        mana = nextMana;
        if (nextMana < MAX_MANA) {
            nextMana += manaPerTurn;
        }
        for (int i = 0; i < cardPerTurn; i++) {
            drawCard();
        }
    }

    String getGameEvents() {
        StringBuilder result = new StringBuilder();
        // todo add limit
        for (GameEvent g : gameHistory.getEvents()) {
            result.append(g.toString());
            result.append("\n");
        }
        return result.toString();
    }

    private void drawCard() {
        Card card = deck.remove((int) (Math.random() * deck.size()));
        gameHistory.getEvents().add(new DrawCard(card));
        handCard.add(card);
    }

    void playCard(Card card) {
        if (handCard.contains(card) && card.getManaFrz() <= mana) {
            mana -= card.getManaFrz();
            handCard.remove(card);
            if (card instanceof Minion) playMinion((Minion) card);
            if (card instanceof Weapon) playWeapon((Weapon) card);
            if (card instanceof Spell) playSpell((Spell) card);
        }
    }

    private void playMinion(Minion minion) {
        ground.add(minion);
        gameHistory.getEvents().add(new PlayCard(minion));
    }

    private void playWeapon(Weapon weapon) {
        activeWeapon = weapon;
        gameHistory.getEvents().add(new PlayCard(weapon));
    }

    private void playSpell(Spell spell) {
        gameHistory.getEvents().add(new PlayCard(spell));
    }


}

