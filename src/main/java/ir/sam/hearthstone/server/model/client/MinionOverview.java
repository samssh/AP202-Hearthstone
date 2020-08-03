package ir.sam.hearthstone.server.model.client;


import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.MinionLogic;

public class MinionOverview extends CardOverview {
    private final boolean hasTaunt, hasRush, hasDivineShield, hasSleep;

    public MinionOverview(MinionLogic minionLogic) {
        super(minionLogic.getName(), minionLogic.getName()
                , "class of card: " + minionLogic.getCard().getClassOfCard().getHeroName()
                , 1, minionLogic.getCard().getPrice(), minionLogic.getCard().getManaFrz()
                , minionLogic.getAttack(), minionLogic.getHp(), false);
        hasTaunt = minionLogic.isHasTaunt();
        hasRush = minionLogic.isHasRush();
        hasDivineShield = minionLogic.isHasDivineShield();
        hasSleep = minionLogic.isHasSleep();
    }

}
