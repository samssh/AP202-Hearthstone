package view.model;

import lombok.Getter;
import model.Deck;

public class DeckOverview {
    @Getter
    private final String deckName,heroName,cardName;
    @Getter
    private final int games,wins;
    @Getter
    private final double winRate,manaAverage;

    public DeckOverview(Deck deck , String cardName) {
        this.deckName = deck.getName();
        this.heroName = deck.getHero().getName();
        this.cardName = cardName;
        this.games = deck.getGames();
        this.wins = deck.getWins();
        this.winRate = deck.getWinRate();
        this.manaAverage = deck.getManaAverage();
    }
}
