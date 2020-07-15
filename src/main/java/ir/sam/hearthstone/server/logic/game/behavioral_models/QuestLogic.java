package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Card;
import ir.sam.hearthstone.model.main.Quest;

public class QuestLogic extends CardLogic{
    private Quest quest;

    public QuestLogic(Quest quest){
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
}
