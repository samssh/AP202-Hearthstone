package model;

import hibernate.Connector;
import hibernate.ManualMapping;
import hibernate.SaveAble;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static model.Models.mage;

@Entity
public class Player implements SaveAble {
    // id nano time
    @Id
    private String userName;
    @Column
    private String password;
    @Column
    private Long creatTime;
    @Column
    private int coin;
    @ManyToOne
    private Hero selectedHero;
    @ElementCollection
    private List<String> cartsId;
    @Transient
    private List<Card> cards;
    @ElementCollection
    private List<String> heroesId;
    @Transient
    private List<Hero> heroes;
    @ElementCollection
    private List<Long> decksId;
    @Transient
    private List<Deck> decks;

    {
        cartsId = new ArrayList<>();
        cards = new ArrayList<>();
        heroes = new ArrayList<>();
        heroesId = new ArrayList<>();
        decks = new ArrayList<>();
        decksId = new ArrayList<>();
    }

    // only hibernate use this constructor
    public Player() {
    }

    public Player(String userName, String password, Long creatTime,
                  int coin, Hero selectedHero, List<Card> cards,
                  List<Hero> heroes, List<Deck> decks) {
        this.userName = userName;
        this.password = password;
        this.creatTime = creatTime;
        this.coin = coin;
        this.selectedHero = selectedHero;
        this.cards = cards;
        this.heroes = heroes;
        this.decks = decks;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public Long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public Hero getSelectedHero() {
        return selectedHero;
    }

    public void setSelectedHero(Hero selectedHero) {
        this.selectedHero = selectedHero;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getCartsId() {
        return cartsId;
    }

    public void setCartsId(List<String> cartsId) {
        this.cartsId = cartsId;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<String> getHeroesId() {
        return heroesId;
    }

    public void setHeroesId(List<String> heroesId) {
        this.heroesId = heroesId;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes;
    }

    public List<Long> getDecksId() {
        return decksId;
    }

    public void setDecksId(List<Long> decksId) {
        this.decksId = decksId;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    public void addCard(Card card) {
        if (cards.contains(card)) cards.add(this.cards.lastIndexOf(card), card);
        else cards.add(card);
    }

    public void removeCard(Card card){
        cards.remove(card);
    }

    public int numberOfCard(Card card) {
        int c = 0;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).equals(card)) {
                c++;
            }
        }
        return c;
    }

    public boolean isInDeck(Card card) {
        for (Deck d : decks)
            if (d.numberOfCard(card) > 0)
                return true;
        return false;
    }


    @Override
    public void delete() {
        Connector connector = Connector.getConnector();
        ManualMapping.deleteList(decks);
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate() {
        Connector connector = Connector.getConnector();
        ManualMapping.saveOrUpdateList(cartsId, cards);
        ManualMapping.saveOrUpdateList(heroesId, heroes);
        ManualMapping.saveOrUpdateList(decksId, decks);
        connector.saveOrUpdate(this);
    }

    @Override
    public void load() {
        setDecks(ManualMapping.fetchList(Deck.class, decksId));
        setHeroes(ManualMapping.fetchList(Hero.class, heroesId));
        setCards((ManualMapping.fetchList(Card.class, cartsId)));
    }

    @Override
    public String getId() {
        return getUserName();
    }
}
