package server;

import lombok.Getter;
import model.account.*;
import model.main.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static server.Server.*;

class Game {
    @Getter
    private final List<Minion> ground;
    @Getter
    private final List<Card> handCard, deck;
    @Getter
    private final GameHistory gameHistory;
    @Getter
    private final Hero hero;
    @Getter
    private Weapon activeWeapon;
    @Getter
    private int mana, nextMana;
    @Getter
    private boolean running;

    Game(Deck deck, Passive passive,Player player) {
        this.hero = deck.getHero();
        this.ground = new ArrayList<>();
        this.deck = deckToList(deck);
        this.gameHistory = new GameHistory(passive, hero, new ArrayList<>(this.deck),player);
        this.handCard = new ArrayList<>();
        for (int i = 0; i < STARTING_HAND_CARDS; i++) {
            drawCard();
        }
        this.mana = STARTING_MANA;
        this.nextMana = this.mana+ MANA_PER_TURN;
        running = true;
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
        gameHistory.getEvents().add(new EndTurn(nextMana,gameHistory));
        mana = nextMana;
        if (nextMana < MAX_MANA) {
            nextMana += MANA_PER_TURN;
        }
        if (deck.size()>0) {
            for (int i = 0; i < CARD_PER_TURN && deck.size()>0; i++) {
                drawCard();
            }
        }else {
            gameHistory.getEvents().add(new EndGame(EndGame.EndGameType.WIN,gameHistory));
            running =false;
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
        gameHistory.getEvents().add(new DrawCard(card,gameHistory));
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
        gameHistory.getEvents().add(new PlayCard(minion,gameHistory));
    }

    private void playWeapon(Weapon weapon) {
        activeWeapon = weapon;
        gameHistory.getEvents().add(new PlayCard(weapon,gameHistory));
    }

    private void playSpell(Spell spell) {
        gameHistory.getEvents().add(new PlayCard(spell,gameHistory));
    }

    void exit(){
        if (running){
            gameHistory.getEvents().add(new EndGame(EndGame.EndGameType.LOSE,gameHistory));
        }
    }

}

