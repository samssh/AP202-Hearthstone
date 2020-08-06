package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.model.account.Deck;
import lombok.Getter;
import lombok.ToString;

public class BigDeckOverview extends Overview {
    @Getter
    private final String cardName;
    @Getter
    private final int games, wins;
    @Getter
    private final double winRate, manaAverage;

    public BigDeckOverview(Deck deck, String cardName) {
        super(deck.getName(), deck.getHero().getName());
        this.cardName = cardName;
        this.games = deck.getGames();
        this.wins = deck.getWins();
        this.winRate = deck.getWinRate();
        this.manaAverage = deck.getManaAverage();
    }

    @Override
    public String toString() {
        return "BigDeckOverview{" +
                "cardName='" + cardName + '\'' +
                ", games=" + games +
                ", wins=" + wins +
                ", winRate=" + winRate +
                ", manaAverage=" + manaAverage +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
