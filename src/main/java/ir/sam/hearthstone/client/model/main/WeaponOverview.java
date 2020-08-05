package ir.sam.hearthstone.client.model.main;

import ir.sam.hearthstone.client.resource_manager.ImageLoader;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WeaponOverview extends CardOverview {
    private final static BufferedImage closeWeapon = ImageLoader.getInstance().getEffect("close weapon");
    @Getter
    @Setter
    private boolean hasAttack;

    public WeaponOverview() {
    }

    @Override
    public BufferedImage getSmall() {
        if (small == null) {
            small = ImageLoader.getInstance().getWeapon(imageName);
        }
        return small;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        if (hasAttack) {
            graphics2D.drawImage(getSmall(), 0, 0, null);
            int w = getSmall().getWidth(), h = getSmall().getHeight();
            graphics2D.setColor(Color.WHITE);
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD));
            graphics2D.setFont(graphics2D.getFont().deriveFont(21.0F));
            graphics2D.drawString("" + hp, 73 * w / 100, 77 * h / 90);
            graphics2D.drawString("" + att, 14 * w / 100, 73 * h / 90);
        } else graphics2D.drawImage(closeWeapon, 0, 0, null);
    }
}