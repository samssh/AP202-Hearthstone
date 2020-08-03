package ir.sam.hearthstone.client.model.main;

import ir.sam.hearthstone.client.resource_manager.ImageLoader;

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

    private boolean hasTaunt, hasRush, hasDivineShield, hasSleep;

    public MinionOverview(String name, String imageName, String toolkit) {
        super(name, imageName, toolkit);
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
