package ir.sam.hearthstone.view.model;

import ir.sam.hearthstone.resource_manager.ImageLoader;
import ir.sam.hearthstone.server.logic.game.behavioral_models.MinionLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MinionOverview extends CardOverview {
    private static final BufferedImage divineShieldImage;
    private static final BufferedImage tauntImage;
    private static final BufferedImage sleepImage;
    private static final BufferedImage rushImage;

    static {
        divineShieldImage = ImageLoader.getInstance().getEffect("divine shield");
        tauntImage = ImageLoader.getInstance().getEffect("taunt");
        sleepImage = ImageLoader.getInstance().getEffect("sleep");
        rushImage = ImageLoader.getInstance().getEffect("rush");
    }

    private final boolean hasTaunt, hasRush, hasDivineShield, hasSleep;

    public MinionOverview(MinionLogic minionLogic) {
        super(minionLogic.getName(), minionLogic.getName()
                , "class of card: " + minionLogic.getCard().getClassOfCard().getHeroName()
                , 1, minionLogic.getCard().getPrice(), minionLogic.getCard().getManaFrz()
                , minionLogic.getAttack(), minionLogic.getHp(), false);
        small = ImageLoader.getInstance().getMinion(imageName);
        hasTaunt = minionLogic.isHasTaunt();
        hasRush = minionLogic.isHasRush();
        hasDivineShield = minionLogic.isHasDivineShield();
        hasSleep = minionLogic.isHasSleep();
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        int w = small.getWidth(), h = small.getHeight();
        if (hasTaunt) graphics2D.drawImage(tauntImage, 0, 0, null);
        graphics2D.drawImage(small, 0, 0, null);
        if (hasDivineShield) graphics2D.drawImage(divineShieldImage, 0, 0, null);
        if (hasSleep) graphics2D.drawImage(sleepImage, 20 * w / 90, 20 * h / 120, null);
        if (hasRush) graphics2D.drawImage(rushImage, 35 * w / 90, 88 * h / 120, null);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD));
        graphics2D.setFont(graphics2D.getFont().deriveFont(21.0F));
        graphics2D.drawString("" + hp, 67 * w / 90, 100 * h / 120);
        graphics2D.drawString("" + att, 12 * w / 90, 100 * h / 120);
    }
}
