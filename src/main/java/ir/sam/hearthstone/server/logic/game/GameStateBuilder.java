package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.server.model.account.Deck;
import ir.sam.hearthstone.server.model.main.*;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.logic.game.behavioral_models.*;
import ir.sam.hearthstone.server.logic.game.events.DrawCard;
import ir.sam.hearthstone.client.view.model.HeroOverview;
import ir.sam.hearthstone.client.view.model.HeroPowerOverview;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

import static ir.sam.hearthstone.server.logic.game.Side.PLAYER_ONE;
import static ir.sam.hearthstone.server.logic.game.Side.PLAYER_TWO;

public class GameStateBuilder {
    @Setter
    @Accessors(chain = true)
    @Getter
    private Passive passiveP1, passiveP2;
    @Setter
    @Accessors(chain = true)
    @Getter
    private Deck deckP1, deckP2;
    @Setter
    @Accessors(chain = true)
    @Getter
    private List<Card> deckCardsP1, deckCardsP2, handP1, handP2;

    public GameState build() {
        GameState gameState = new GameState();
        gameState.setSideTurn(PLAYER_TWO);
        gameState.setTurnNumber(0);
        buildSideState(PLAYER_ONE, passiveP1, deckP1, deckCardsP1, handP1, gameState);
        buildSideState(PLAYER_TWO, passiveP2, deckP2, deckCardsP2, handP2, gameState);
        return gameState;
    }

    private void buildSideState(Side side, Passive passive, Deck deck, List<Card> deckCards
            , List<Card> hand, GameState gameState) {
        gameState.setPassive(side, new PassiveLogic(passive, side));
        gameState.setHero(side, new HeroLogic(side, deck.getHero()));
        gameState.getEvents().add(new PlayDetails.EventBuilder(PlayDetails.EventType.SET_HERO)
                .setOverview(new HeroOverview(gameState.getHero(side))).setSide(side.getIndex()).build());
        gameState.setHeroPower(side, new HeroPowerLogic(side, deck.getHero().getPower()));
        gameState.getEvents().add(new PlayDetails.EventBuilder(PlayDetails.EventType.SET_HERO_POWER)
                .setOverview(new HeroPowerOverview(deck.getHero().getPower())).setSide(side.getIndex()).build());
        gameState.setMana(side, 0);
        deckCards.forEach(card -> gameState.getDeck(side).add(buildCardLogic(side, card)));
        hand.forEach(card -> gameState.getHand(side).add(buildCardLogic(side, card)));
        hand.forEach(card -> gameState.getGameEvents().add(new DrawCard(side, card)));
    }

    private CardLogic buildCardLogic(Side side, Card card) {
        if (card instanceof Minion) return new MinionLogic(side, (Minion) card);
        else if (card instanceof Weapon) return new WeaponLogic(side, (Weapon) card);
        else if (card instanceof Quest) return new QuestLogic(side, (Quest) card);
        else if (card instanceof Spell) return new SpellLogic(side, (Spell) card);
        else return null;
    }

}
