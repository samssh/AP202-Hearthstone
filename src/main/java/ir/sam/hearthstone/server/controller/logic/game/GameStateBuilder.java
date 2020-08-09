package ir.sam.hearthstone.server.controller.logic.game;

import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.*;
import ir.sam.hearthstone.server.controller.logic.game.events.DrawCard;
import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.client.HeroOverview;
import ir.sam.hearthstone.server.model.client.HeroPowerOverview;
import ir.sam.hearthstone.server.model.main.*;
import ir.sam.hearthstone.server.model.response.PlayDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ir.sam.hearthstone.server.controller.logic.game.Side.PLAYER_ONE;
import static ir.sam.hearthstone.server.controller.logic.game.Side.PLAYER_TWO;

public class GameStateBuilder {
    private final Map<Side, SideStateBuilder> stateBuilderMap;

    public GameStateBuilder() {
        stateBuilderMap = new HashMap<>();
        stateBuilderMap.put(PLAYER_ONE, new SideStateBuilder());
        stateBuilderMap.put(PLAYER_TWO, new SideStateBuilder());
    }

    public GameState build() {
        GameState gameState = new GameState();
        gameState.setSideTurn(PLAYER_ONE);
        gameState.setTurnNumber(0);
        buildSideState(PLAYER_ONE, stateBuilderMap.get(PLAYER_ONE), gameState);
        buildSideState(PLAYER_TWO, stateBuilderMap.get(PLAYER_ONE), gameState);
        return gameState;
    }

    public Passive getPassive(Side side) {
        return stateBuilderMap.get(side).passive;
    }

    public GameStateBuilder setPassive(Side side, Passive passive) {
        this.stateBuilderMap.get(side).passive = passive;
        return this;
    }

    public Deck getDeck(Side side) {
        return stateBuilderMap.get(side).deck;
    }

    public GameStateBuilder setDeck(Side side, Deck deck) {
        this.stateBuilderMap.get(side).deck = deck;
        return this;
    }

    public List<Card> getDeckCards(Side side) {
        return stateBuilderMap.get(side).deckCards;
    }

    public GameStateBuilder setDeckCards(Side side, List<Card> deckCards) {
        this.stateBuilderMap.get(side).deckCards = deckCards;
        return this;
    }

    public List<Card> getHand(Side side) {
        return stateBuilderMap.get(side).hand;
    }

    public GameStateBuilder setHand(Side side, List<Card> hand) {
        this.stateBuilderMap.get(side).hand = hand;
        return this;
    }

    private static class SideStateBuilder {
        private Passive passive;
        private Deck deck;
        private List<Card> deckCards, hand;
    }

    private void buildSideState(Side side, SideStateBuilder sideStateBuilder, GameState gameState) {
        gameState.setPassive(side, new PassiveLogic(sideStateBuilder.passive, side));
        gameState.setHero(side, new HeroLogic(side, sideStateBuilder.deck.getHero()));
        gameState.getEvents().add(new PlayDetails.EventBuilder(PlayDetails.EventType.SET_HERO)
                .setOverview(new HeroOverview(gameState.getHero(side))).setSide(side.getIndex()).build());
        gameState.setHeroPower(side, new HeroPowerLogic(side, sideStateBuilder.deck.getHero().getHeroPower()));
        gameState.getEvents().add(new PlayDetails.EventBuilder(PlayDetails.EventType.SET_HERO_POWER)
                .setOverview(new HeroPowerOverview(sideStateBuilder.deck.getHero().getHeroPower()))
                .setSide(side.getIndex()).build());
        gameState.setMana(side, 0);
        sideStateBuilder.deckCards.forEach(card -> gameState.getDeck(side).add(buildCardLogic(side, card)));
        sideStateBuilder.hand.forEach(card -> gameState.getHand(side).add(buildCardLogic(side, card)));
        sideStateBuilder.hand.forEach(card -> gameState.getGameEvents().add(new DrawCard(side, card)));
    }

    private CardLogic buildCardLogic(Side side, Card card) {
        if (card instanceof Minion) return new MinionLogic(side, (Minion) card);
        else if (card instanceof Weapon) return new WeaponLogic(side, (Weapon) card);
        else if (card instanceof Quest) return new QuestLogic(side, (Quest) card);
        else if (card instanceof Spell) return new SpellLogic(side, (Spell) card);
        else return null;
    }

}
