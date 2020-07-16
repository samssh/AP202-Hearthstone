package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Quest;
import ir.sam.hearthstone.server.logic.game.Side;
import lombok.Getter;

public class QuestLogic extends CardLogic {
    @Getter
    protected Quest quest;

    public QuestLogic(Side side, Quest quest) {
        super(side);
        this.quest = quest.clone();
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
    public String toString() {
        return "QuestLogic{" +
                "quest=" + quest +
                '}';
    }
}
