package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.model.account.Deck;
import ir.sam.hearthstone.model.main.*;
import ir.sam.hearthstone.server.logic.game.behavioral_models.*;
import lombok.AccessLevel;
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
        gameState.setSideTurn(PLAYER_ONE);
        gameState.setTurnNumber(0);
        buildSideState(PLAYER_ONE, passiveP1, deckP1,deckCardsP1,handP1, gameState);
        buildSideState(PLAYER_TWO, passiveP2, deckP2,deckCardsP1,handP1, gameState);
        return gameState;
    }

    private void buildSideState(Side side, Passive passive, Deck deck, List<Card> deckCards
            , List<Card> hand, GameState gameState) {
        gameState.setPassive(side, new PassiveLogic(passive));
        gameState.setHero(side, new HeroLogic(deck.getHero()));
        gameState.setHeroPower(side, new HeroPowerLogic(deck.getHero().getPower()));
        gameState.setMana(side, 1);
        deckCards.forEach(card -> gameState.getDeck(side).add(buildCardLogic(card)));
        hand.forEach(card -> gameState.getHand(side).add(buildCardLogic(card)));
    }

    private CardLogic buildCardLogic(Card card) {
        if (card instanceof Minion) return new MinionLogic((Minion) card);
        else if (card instanceof Weapon) return new WeaponLogic((Weapon) card);
        else if (card instanceof Quest) return new QuestLogic((Quest) card);
        else if (card instanceof Spell) return new SpellLogic((Spell) card);
        else return null;
    }

}
