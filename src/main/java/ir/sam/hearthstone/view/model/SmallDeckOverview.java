package ir.sam.hearthstone.view.model;

import ir.sam.hearthstone.model.account.Deck;
import ir.sam.hearthstone.resource_manager.ImageLoader;
import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;
@ToString(exclude = {"image"})
public class SmallDeckOverview extends Overview{
    private final BufferedImage image;
    public SmallDeckOverview(Deck deck) {
        super(deck.getName(),deck.getHero().getName());
        image = ImageLoader.getInstance().getSmallDeck(imageName);
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawImage(image, 0, 0, null);
        g.setFont(new Font("War Priest 3D", Font.PLAIN, 15));
        g.setColor(Color.yellow);
        g.drawString("deck name:" + name, 0, 20);
        g.drawString("hero name:" + imageName, 0, 40);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }
}
