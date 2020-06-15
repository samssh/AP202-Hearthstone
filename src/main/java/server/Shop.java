package server;

import client.Client;
import hibernate.Connector;
import model.account.Player;
import model.log.ResponseLog;
import model.log.BuySellLog;
import model.main.Card;
import response.Response;
import response.ShopDetails;
import resourceManager.ModelLoader;
import view.model.CardOverview;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Shop {
    private final Connector connector;
    private final ModelLoader modelLoader;

    public Shop(Connector connector, ModelLoader modelLoader) {
        this.connector = connector;
        this.modelLoader = modelLoader;
    }

    public Response sendShop(Player player) {
        return new ShopDetails(makeSellList(player), makeBuyList(player), player.getCoin());
    }

    private List<CardOverview> makeSellList(Player player) {
        List<CardOverview> result = new ArrayList<>();
        player.getCards().keySet().forEach(card -> result.add(new CardOverview(card,
                player.getCards().get(card).getRepeatedTimes(), true)));
        return result;
    }

    private List<CardOverview> makeBuyList(Player player) {
        List<CardOverview> buyList = new ArrayList<>();
        List<Card> availableCards = availableCards(player);
        availableCards.stream().filter(player.getCards()::containsKey)
                .filter(c -> player.getCards().get(c).getRepeatedTimes() == 1)
                .filter(c -> c.getPrice() <= player.getCoin())
                .forEach(c -> buyList.add(new CardOverview(c, 1, true)));
        availableCards.stream().filter(c -> !player.getCards().containsKey(c))
                .filter(c -> 2 * c.getPrice() <= player.getCoin())
                .forEach(c -> buyList.add(new CardOverview(c, 2, true)));
        availableCards.stream().filter(c -> !player.getCards().containsKey(c))
                .filter(c -> 2 * c.getPrice() > player.getCoin())
                .filter(c -> c.getPrice() <= player.getCoin())
                .forEach(c -> buyList.add(new CardOverview(c, 1, true)));
        return buyList;
    }

    private List<Card> availableCards(Player player) {
        List<Card> result = new ArrayList<>();
        List<Card> cards = modelLoader.getCards();
        cards.stream().filter(card -> card.getClassOfCard().getHeroName().equals("Neutral")
                || containHero(card.getClassOfCard().getHeroName(), player)).forEach(result::add);
        return result;
    }

    private boolean containHero(String heroName, Player player) {
        return player.getHeroes().contains(modelLoader.getHero(heroName).orElse(null));
    }

    public Response sellCard(String cardName, Player player) {
        Optional<Card> optionalCard = modelLoader.getCard(cardName);
        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            if (canSell(card, player)) {
                player.setCoin(player.getCoin() + card.getPrice());
                player.removeCard(card);
                connector.save(player);
                connector.save(new BuySellLog(player.getUserName()
                        , player.getCoin() - card.getPrice(), player.getCoin(), cardName, "sell"));
            }
            return sendShop(player);
        }
        return null;
    }

    private boolean canSell(Card card, Player player) {
        return player.getCards().containsKey(card);
    }

    public Response buyCard(String cardName, Player player) {
        Optional<Card> optionalCard = modelLoader.getCard(cardName);
        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            if (canBuy(card, player)) {
                player.setCoin(player.getCoin() - card.getPrice());
                player.addCard(card);
                connector.save(player);
                connector.save(new BuySellLog(player.getUserName()
                        , player.getCoin() + card.getPrice(), player.getCoin(), cardName, "buy"));
            }
            return sendShop(player);
        }
        return null;
    }

    public boolean canBuy(Card card, Player player) {
        return availableCards(player).contains(card) && player.getCoin() >= card.getPrice() && player.numberOfCard(card) < 2;
    }

}
