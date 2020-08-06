package ir.sam.hearthstone.client.model.main;

import java.awt.image.BufferedImage;

public class CardOverview extends AbstractCardOverview {
    public CardOverview() {
    }

    public CardOverview(String name, String imageName, String toolkit, int number, int price, int mana
            , int att, int hp, boolean showPrice, BufferedImage big, BufferedImage small) {
        super(name, imageName, toolkit, number, price, mana, att, hp, showPrice, big, small);
    }
}
