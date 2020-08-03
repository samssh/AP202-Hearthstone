package ir.sam.hearthstone.server.model.client;

import ir.sam.hearthstone.server.model.account.Deck;
import lombok.ToString;

@ToString(includeFieldNames = false)
public class BigDeckOverview extends Overview {
    private final String cardName;
    private final int games, wins;
    private final double winRate, manaAverage;

    public BigDeckOverview(Deck deck, String cardName) {
        super(deck.getName(), deck.getHero().getName());
        this.cardName = cardName;
        this.games = deck.getGames();
        this.wins = deck.getWins();
        this.winRate = deck.getWinRate();
        this.manaAverage = deck.getManaAverage();
    }
}
