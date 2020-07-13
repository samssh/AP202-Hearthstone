package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.Quest;

public class QuestLogic extends CardLogic{
    private Quest quest;

    public QuestLogic(Quest quest){
        this.quest = quest.clone();
    }
}
