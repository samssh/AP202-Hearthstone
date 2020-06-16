package ir.SAM.hearthstone.server;

import ir.SAM.hearthstone.hibernate.Connector;
import lombok.Getter;
import ir.SAM.hearthstone.model.account.*;
import ir.SAM.hearthstone.model.log.InGameLog;
import ir.SAM.hearthstone.model.main.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static ir.SAM.hearthstone.server.Server.*;

class Game {
    @Getter
    private final List<Minion> ground;
    @Getter
    private final List<Card> handCard, deck;
    private final List<GameEvent> gameEvents;
    @Getter
    private final Hero hero;
    @Getter
    private Weapon activeWeapon;
    @Getter
    private int mana, nextMana;
    @Getter
    private boolean running;
    private final Player player;
    private final Connector connector;

    Game(Deck deck, Passive passive, Player player, Connector connector) {
        this.connector = connector;
        this.hero = deck.getHero();
        this.ground = new LinkedList<>();
        this.deck = deckToList(deck);
        this.player = player;
        this.gameEvents= new LinkedList<>();
        this.handCard = new LinkedList<>();
        for (int i = 0; i < STARTING_HAND_CARDS; i++) {
            drawCard();
        }
        this.mana = STARTING_MANA;
        this.nextMana = this.mana + MANA_PER_TURN;
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
        gameEvents.add(new EndTurn(nextMana));
        connector.save(new InGameLog(player.getUserName(), null, "next turn", nextMana));
        mana = nextMana;
        if (nextMana < MAX_MANA) {
            nextMana += MANA_PER_TURN;
        }
        if (deck.size() > 0) {
            for (int i = 0; i < CARD_PER_TURN && deck.size() > 0; i++) {
                drawCard();
            }
        } else {
            gameEvents.add(new EndGame(EndGame.EndGameType.WIN));
            connector.save(new InGameLog(player.getUserName(), null, "game ended:win"));
            running = false;
        }
    }

    String getGameEvents() {
        StringBuilder result = new StringBuilder();
        for (GameEvent g : gameEvents) {
            result.append(g.toString());
            result.append("\n");
        }
        return result.toString();
    }

    private void drawCard() {
        Card card = deck.remove((int) (Math.random() * deck.size()));
        if (handCard.size() < 12) {
            handCard.add(card);
            gameEvents.add(new DrawCard(card));
            connector.save(new InGameLog(player.getUserName(), card.getName(), "draw card", mana));
        } else {
            gameEvents.add(new DeleteCard(card));
            connector.save(new InGameLog(player.getUserName(), card.getName(), "delete card", mana));
        }
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
        if (ground.size() < 7) {
            ground.add(minion);
            connector.save(new InGameLog(player.getUserName(), minion.getName(), "play card", mana));
            gameEvents.add(new PlayCard(minion));
        }else {
            connector.save(new InGameLog(player.getUserName(), minion.getName(), "delete card", mana));
            gameEvents.add(new DeleteCard(minion));
        }
    }

    private void playWeapon(Weapon weapon) {
        activeWeapon = weapon;
        connector.save(new InGameLog(player.getUserName(), weapon.getName(), "play card", mana));
        gameEvents.add(new PlayCard(weapon));
    }

    private void playSpell(Spell spell) {
        connector.save(new InGameLog(player.getUserName(), spell.getName(), "play card", mana));
        gameEvents.add(new PlayCard(spell));
    }

    void exit() {
        if (running) {
            gameEvents.add(new EndGame(EndGame.EndGameType.LOSE));
            connector.save(new InGameLog(player.getUserName(), null, "game ended:lose"));
        }
    }

}

