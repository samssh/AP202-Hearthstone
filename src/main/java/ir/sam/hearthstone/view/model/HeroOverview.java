package ir.sam.hearthstone.view.model;

import ir.sam.hearthstone.model.main.Hero;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import ir.sam.hearthstone.server.logic.game.behavioral_models.HeroLogic;
import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

@ToString(exclude = {"big", "small"})
public class HeroOverview extends UnitOverview {
    static final BufferedImage defImage = ImageLoader.getInstance().getEffect("defence");
    private final int hp;
    private final int defence;
    private final BufferedImage big, small;

    public HeroOverview(HeroLogic heroLogic) {

        super(heroLogic.getName(), heroLogic.getName(), null);
        hp = heroLogic.getHp();
        defence = heroLogic.getDefence();
        small = ImageLoader.getInstance().getSmallHero(imageName);
        big = ImageLoader.getInstance().getBigHero(imageName);
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawImage(small, 0, 0, null);
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(19.5F));
        int w = small.getWidth();
        int h = small.getHeight();
        if (defence > 0) {
            g.drawImage(defImage, 87 * w / 120, 65 * h / 170, null);
            g.drawString(defence + "", 96 * w / 120, 88 * h / 170);
        }
        g.drawString(hp + "", 93 * w / 120, 122 * h / 170);
    }

    @Override
    public int getWidth() {
        return small.getWidth();
    }

    @Override
    public int getHeight() {
        return small.getHeight();
    }

    @Override
    public Image getBigImage() {
        ColorModel cm = big.getColorModel();
        BufferedImage image = new BufferedImage(cm, big.copyData(null), cm.isAlphaPremultiplied(), null);
        Graphics g = image.createGraphics();
        this.paintBig(g);
        return image;
    }

    private void paintBig(Graphics g) {
        int w = big.getWidth();
        int h = big.getHeight();
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setFont(g.getFont().deriveFont(40.0F));
        g.drawString(hp + "", 192 * w / 250, 250 * h / 350);
    }
}
