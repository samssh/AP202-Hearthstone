package ir.SAM.hearthstone.view.model;

import ir.SAM.hearthstone.model.account.Deck;
import ir.SAM.hearthstone.resourceManager.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SmallDeckOverview extends Overview{
    private final BufferedImage image;
    public SmallDeckOverview(Deck deck) {
        super(deck.getName(),deck.getHero().getName());
        image = ImageLoader.getInstance().getSmallDeck(imageName);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
        g.setFont(new Font("War Priest 3D", Font.PLAIN, 15));
        g.setColor(Color.yellow);
        g.drawString("deck name:" + name, 0, 20);
        g.drawString("hero name:" + imageName, 0, 40);
    }

    @Override
    public String toString() {
        return "SmallDeckOverview(" +
                "name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                ')';
    }
}
