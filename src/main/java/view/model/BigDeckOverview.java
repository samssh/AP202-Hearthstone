package view.model;

import model.account.Deck;
import util.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class BigDeckOverview extends Overview {
    private final String cardName;
    private final int games,wins;
    private final double winRate,manaAverage;
    private final BufferedImage image;

    public BigDeckOverview(Deck deck , String cardName) {
        super(deck.getName(),deck.getHero().getName());
        this.cardName = cardName;
        this.games = deck.getGames();
        this.wins = deck.getWins();
        this.winRate = deck.getWinRate();
        this.manaAverage = deck.getManaAverage();
        image = ImageLoader.getInstance().getBigDeck(imageName);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
        g.setFont(new Font("War Priest Expanded", Font.PLAIN, 20));
        g.setColor(Color.yellow);
        g.drawString("deck name:" + name, 0, 40);
        g.drawString("hero name:" + imageName, 0, 80);
        String s;
        if (winRate != -1) s = new DecimalFormat("#.##").format(winRate);
        else s = "--";
        g.drawString("wins:" + wins + " games:" + games + " winRate:" + s, 0, 120);
        String p;
        if (manaAverage != -1) {
            p = new DecimalFormat("#.##").format(manaAverage);
            g.drawString("mana average:" + p, 0, 160);
            g.drawString("MVC:" + cardName, 0, 200);
        } else g.drawString("deck empty", 0, 160);
    }

    @Override
    public String toString() {
        return "BigDeckOverview(" +
                "name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                ", cardName='" + cardName + '\'' +
                ", games=" + games +
                ", wins=" + wins +
                ", winRate=" + winRate +
                ", manaAverage=" + manaAverage +
                ')';
    }
}
