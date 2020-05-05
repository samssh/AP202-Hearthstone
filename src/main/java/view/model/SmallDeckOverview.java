package view.model;

import model.Deck;
import util.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SmallDeckOverview extends Overview{
    public SmallDeckOverview(Deck deck) {
        super(deck.getName(),deck.getHero().getName());
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage image = ImageLoader.getInstance().getSmallDeck(imageName);
        g.drawImage(image, 0, 0, null);
        g.setFont(new Font("War Priest 3D", Font.PLAIN, 15));
        g.setColor(Color.yellow);
        g.drawString("deck name:" + name, 0, 20);
        g.drawString("hero name:" + imageName, 0, 40);
    }
}
