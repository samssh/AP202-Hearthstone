package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Quest;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.GameState;
import ir.sam.hearthstone.server.logic.game.Side;
import ir.sam.hearthstone.server.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.logic.game.events.PlayCard;
import lombok.Getter;
import lombok.Setter;

public class QuestLogic extends CardLogic {
    @Getter
    protected Quest quest;
    @Getter
    @Setter
    protected int questProgress;

    public QuestLogic(Side side, Quest quest) {
        super(side);
        this.quest = quest.clone();
        questProgress = 0;
    }

    @Override
    public String getName() {
        return quest.getName();
    }

    @Override
    public Card getCard() {
        return quest;
    }

    @Override
    public void play(AbstractGame game) {
        GameState gameState = game.getGameState();
        int sideMana = gameState.getMana(side);
        if (sideMana >= quest.getManaFrz()) {
            int indexOnHand = gameState.getHand(side).indexOf(this);
            gameState.setMana(side, sideMana - quest.getManaFrz());
            gameState.setActiveQuest(side, this);
            GameEvent gameEvent = new PlayCard(side, quest);
            gameState.getHand(side).remove(indexOnHand);
            gameState.getGameEvents().add(gameEvent);
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.PLAY_SPELL)
                    .setSide(side.getIndex()).setIndex(indexOnHand).build();
            gameState.getEvents().add(event);
            game.getActionHolderMap().get(ActionType.DO_ACTION).doAction(this, this, game);
        }
    }

    @Override
    public String toString() {
        return "QuestLogic{" +
                "quest=" + quest +
                '}';
    }
}
