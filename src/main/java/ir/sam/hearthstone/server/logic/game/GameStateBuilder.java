package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.model.account.Deck;
import ir.sam.hearthstone.model.main.*;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.logic.game.behavioral_models.*;
import ir.sam.hearthstone.server.logic.game.events.DrawCard;
import ir.sam.hearthstone.view.model.CardOverview;
import ir.sam.hearthstone.view.model.HeroOverview;
import ir.sam.hearthstone.view.model.HeroPowerOverview;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

import static ir.sam.hearthstone.server.logic.game.Side.*;

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
        gameState.setPassive(side, new PassiveLogic(passive));
        gameState.setHero(side, new HeroLogic(deck.getHero()));
        gameState.getEvents().add(new PlayDetails.Event(PlayDetails.EventType.SET_HERO,
                new HeroOverview(deck.getHero()),side.getIndex()));
        gameState.setHeroPower(side, new HeroPowerLogic(deck.getHero().getPower()));
        gameState.getEvents().add(new PlayDetails.Event(PlayDetails.EventType.SET_HERO_POWER,
                new HeroPowerOverview(deck.getHero().getPower()),side.getIndex()));
        gameState.setMana(side, 0);
        deckCards.forEach(card -> gameState.getDeck(side).add(buildCardLogic(card)));
        hand.forEach(card -> gameState.getHand(side).add(buildCardLogic(card)));
        hand.forEach(card -> gameState.getEvents().add(new PlayDetails.Event(PlayDetails.EventType.ADD_TO_HAND
                , new CardOverview(card, 1, false), side.getIndex())));
        hand.forEach(card->gameState.getGameEvents().add(new DrawCard(side,card)));
    }

    private CardLogic buildCardLogic(Card card) {
        if (card instanceof Minion) return new MinionLogic((Minion) card);
        else if (card instanceof Weapon) return new WeaponLogic((Weapon) card);
        else if (card instanceof Quest) return new QuestLogic((Quest) card);
        else if (card instanceof Spell) return new SpellLogic((Spell) card);
        else return null;
    }

}
