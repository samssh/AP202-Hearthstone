package contoroller;

import hibernate.Connector;
import hibernate.ManualMapping;
import model.*;
import view.Console;

import java.util.ArrayList;
import java.util.List;

public class MenuController {
    private Menu menu;
    private Player player;
    private int i = -1;

    public MenuController(Menu menu) {
        this.menu = menu;
    }

    public void run() {
        while (true) {
//            viewer.view(menu);
            action();
            Console console = Console.getConsole();
            if (i >= 0) {
                menu = menu.getMenuList().get(i);
                i = -1;
                continue;
            }
            boolean end=true;
            while (end) {
                String s = console.read();
                for (Menu m : menu.getMenuList()) {
                    if (m.isHasEntryList()) {
                        if (m.getKey().length() <= s.length() &&
                                m.getKey().equals(s.substring(0, m.getKey().length()))) {
                            String k = s.substring(m.getKey().length()).trim();
                            for (String t : m.getEntryList()) {
                                if (t.equals(k)) {
                                    m.setEntry(k);
                                    break;
                                }
                            }
                            end=false;
                            break;
                        }

                    } else {
                        if (m.getKey().equals(s)) {
                            menu = m;
                            end=false;
                            break;
                        }
                    }
                }
                //incorrect input state
            }
        }
    }

    private void action() {
        switch (menu.getName()) {
            case "welcome":
                welcome();
                break;
            case "signUp":
                signUp();
                break;
            case "login":
                login();
                break;
            case "main":
                main();
                break;
            case "logOut":
                logOut();
                break;
            case "deleteAccount":
                deleteAccount();
                break;
            case "exit":
                exit();
                break;
            case "helpMain":
                helpMain();
                break;
            case "store":
                store();
                break;
            case "buy":
                buy();
                break;
            case "sell":
                sell();
                break;
            case "wallet":
                wallet();
                break;
            case "buyAble":
                buyAble();
                break;
            case "sellAble":
                sellAble();
                break;
            case "helpCollection":
                helpCollection();
                break;
            case "helpStore":
                helpStore();
                break;
            case "collection":
                collection();
                break;
            case "back":
                back();
                break;
            case "openHero":
                openHero();
                break;
            case "defaultHero":
                defaultHero();
                break;
            case "selectHero":
                selectHero();
                break;
            case "availableCard":
                availableCard();
                break;
            case "deckCards":
                deckCards();
                break;
            case "noDeckCard":
                noDeckCard();
                break;
            case "allCard":
                allCard();
                break;
            case "addCard":
                addCard();
                break;
            case "removeCard":
                removeCard();
                break;
            case "detailStore":
                detailStore();
                break;
            case "detailCollection":
                detailCollection();
                break;
        }


    }

    private void detailCollection() {
        Console.getConsole().print(Models.searchUnit(menu.getEntry()).toString());
        i=0;
    }

    private void detailStore() {
        Console.getConsole().print(Models.searchUnit(menu.getEntry()).toString());
        i=0;
    }

    private void removeCard() {
        Card card=Models.searchCard(menu.getEntry());
        if (player.getCards().contains(card)){
            if (player.getSelectedDeck().numberOfCard(card)>0){
                player.getSelectedDeck().removeCard(card);
                Console.getConsole().print("card removed");
            }
            else {
                Console.getConsole().print("you cant add this card to your deck");
            }
        }
        else {
            Console.getConsole().print("you dont have this card");
        }
        i=0;
    }

    private void addCard() {
        Card card=Models.searchCard(menu.getEntry());
        if (player.getCards().contains(card)){
            if (card.getClassOfCard().isItForHero(player.getSelectedHero())
                    && player.getSelectedDeck().numberOfCard(card) < 2){
                player.getSelectedDeck().addCard(card);
                Console.getConsole().print("card added");
            }
            else {
                Console.getConsole().print("you cant add this card to your deck");
            }
        }
        else {
            Console.getConsole().print("you dont have this card");
        }
        i=0;
    }

    private void allCard() {
        List<String> list = new ArrayList<>();
        List<Card> cards = player.getCards();
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            if (i < cards.size() - 1 && c.equals(cards.get(i + 1))) {
                list.add(c.getName() + "(2)");
                i++;
                continue;
            }
            list.add(c.getName());
        }
        if (list.size() == 0)
            Console.getConsole().print("no card for your hero");
        else {
            Console.getConsole().print("the card for your hero:");
            for (String str : list)
                Console.getConsole().print(str);
        }
        i=0;
    }

    private void noDeckCard() {
        List<String> list = new ArrayList<>();
        List<Card> cards = player.getCards();
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            if (c.getClassOfCard().isItForHero(player.getSelectedHero())
                    && player.getSelectedDeck().numberOfCard(c) < 2) {
                list.add(c.getName());
                if (i < cards.size() - 1 && c.equals(cards.get(i + 1))) {
                    i++;
                }

            }
        }
        if (list.size() == 0)
            Console.getConsole().print("no card to add");
        else {
            Console.getConsole().print("the card that you can add to your deck:");
            for (String str : list)
                Console.getConsole().print(str);
        }
        i=0;
    }

    private void deckCards() {
        List<String> list = new ArrayList<>();
        List<Card> cList = player.getSelectedDeck().getCardList();
        for (int i = 0; i < cList.size(); i++) {
            if (i < cList.size() - 1) {
                if (cList.get(i).equals(cList.get(i + 1))) {
                    list.add(cList.get(i).getName() + "(2)");
                    i++;
                    continue;
                }
            }
            list.add(cList.get(i).getName());
        }
        if (list.size() == 0)
            Console.getConsole().print("no card to Deck");
        else {
            Console.getConsole().print("the card in your deck:");
            for (String str : list)
                Console.getConsole().print(str);
        }
        i=0;
    }

    private void availableCard() {
        List<String> list = new ArrayList<>();
        List<Card> cards = player.getCards();
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            if (c.getClassOfCard().isItForHero(player.getSelectedHero())) {
                if (i < cards.size() - 1 && c.equals(cards.get(i + 1))) {
                    list.add(c.getName() + "(2)");
                    i++;
                    continue;
                }
                list.add(c.getName());
            }
        }
        if (list.size() == 0)
            Console.getConsole().print("no card for your hero");
        else {
            Console.getConsole().print("the card for your hero:");
            for (String str : list)
                Console.getConsole().print(str);
        }
        i=0;
    }

    private void selectHero() {
        for (Hero h : player.getHeroes())
            if (h.getName().equals(menu.getEntry())) {
                if (!h.equals(player.getSelectedHero())) {
                    player.setSelectedHero(h);
                    player.setSelectedDeck(player.getHeroDeck(h));
                    Console.getConsole().print("your default hero has changed to " + h.getName());
                    i = 0;
                    return;
                } else {
                    Console.getConsole().print("your hero already is " + h.getName());
                    i = 0;
                    return;
                }
            }
        Console.getConsole().print("this hero is locked");
        i = 0;
    }

    private void defaultHero() {
        Console.getConsole().print("your default hero is:");
        Console.getConsole().print(player.getSelectedHero().getName());
        i = 0;
    }

    private void openHero() {
        Console.getConsole().print("your hero is:");
        for (Hero h : player.getHeroes())
            Console.getConsole().print(h.getName());
        i = 0;
    }

    private void back() {
        i = 0;
    }

    private void collection() {
        Console.getConsole().print("Collection");
    }

    private void helpStore() {
        // keys most be printed
        Console.getConsole().print("help");
        i = 0;
    }

    private void helpCollection() {
        // keys most be printed
        Console.getConsole().print("help");
        i = 0;
    }

    private void sellAble() {
        List<String> list = new ArrayList<>();
        for (Card c : player.getCards()) {
            if (player.isInDeck(c))
                list.add(c.getName());
        }
        if (list.size() == 0)
            Console.getConsole().print("nothing to sell!!!");
        else {
            Console.getConsole().print("you can sell these:");
            for (String str : list) {
                Console.getConsole().print(str);
            }
        }
        i = 0;

    }

    private void buyAble() {
        List<String> list = new ArrayList<>();
        for (Card c : Models.cards) {
            if (c.getPrice() <= player.getCoin() && player.numberOfCard(c) < 2)
                list.add(c.getName());
        }
        if (list.size() == 0)
            Console.getConsole().print("nothing to buy!!!");
        else {
            Console.getConsole().print("you can buy these:");
            for (String str : list) {
                Console.getConsole().print(str);
            }
        }
        i = 0;
    }

    private void wallet() {
        Console.getConsole().print("your coin is: " + player.getCoin());
        i = 0;
    }

    private void sell() {
        Card c = Models.searchCard(menu.getEntry());
        if (player.getCards().contains(c)) {
            if (!player.isInDeck(c)) {
                player.removeCard(c);
                player.setCoin(player.getCoin() + c.getPrice());
                Console.getConsole().print("sell card done");
            } else {
                Console.getConsole().print("you cant sell this card because this card in your deck");
            }
        } else {
            Console.getConsole().print("you dont have this card");
        }
        i = 0;
    }

    private void buy() {
        Card c = Models.searchCard(menu.getEntry());
        if (c.getPrice() <= player.getCoin()) {
            if (player.numberOfCard(c) < 2) {
                player.addCard(c);
                player.setCoin(player.getCoin() - c.getPrice());
                Console.getConsole().print("buy card done");
            } else {
                Console.getConsole().print("you cant buy this because you have 2 of this");
            }
        } else {
            Console.getConsole().print("you dont have enough coin to buy this");
        }
        i = 0;
    }

    private void store() {
        Console.getConsole().print("Store");
    }

    private void helpMain() {
        // keys most be printed
        Console.getConsole().print("help");
        i = 0;
    }

    private void exit() {
        Connector connector = Connector.getConnector();
        connector.open();
        connector.beginTransaction();
        if (player != null)
            player.saveOrUpdate();
        connector.commit();
        connector.close();
        System.exit(0);
    }

    private void deleteAccount() {
        Connector connector = Connector.getConnector();
        connector.open();
        connector.beginTransaction();
        player.delete();
        player = null;
        connector.commit();
        connector.close();
        i = 0;
    }

    private void logOut() {
        Connector connector = Connector.getConnector();
        connector.open();
        connector.beginTransaction();
        player.saveOrUpdate();
        player = null;
        connector.commit();
        connector.close();
        i = 0;
    }

    private void main() {
        Console.getConsole().print("main menu");
    }

    private void login() {
        while (true) {
            Console.getConsole().print("enter user name");
            String s = Console.getConsole().read();
            if ("&".equals(s)) {
                i = 0;
                return;
            }
            Connector connector=Connector.getConnector();
            connector.open();
            Player p = (Player) ManualMapping.fetch(Player.class, s);
            connector.close();
            if (p != null) {
                while (true) {
                    Console.getConsole().print("enter password");
                    String pass1 = Console.getConsole().read();
                    if ("&".equals(pass1)) {
                        i = 0;
                        return;
                    }
                    if (pass1.equals(p.getPassword())) {
                        player = p;
                        i = 1;
                        return;
                    } else {
                        Console.getConsole().print("password wrong");
                        Console.getConsole().print("Enter again or Enter '&' to back");
                    }
                }
            } else {
                Console.getConsole().print("this user not exist enter another");
                Console.getConsole().print("Enter again or Enter '&' to back");
            }
        }
    }

    private void signUp() {
        while (true) {
            Console.getConsole().print("enter user name");
            String s = Console.getConsole().read();
            if ("&".equals(s)) {
                i = 0;
                return;
            }
            Connector connector=Connector.getConnector();
            connector.open();
            Player p = (Player) ManualMapping.fetch(Player.class, s);
            connector.close();
            if (p == null) {
                while (true) {
                    Console.getConsole().print("enter password");
                    String pass1 = Console.getConsole().read();
                    if ("&".equals(pass1)) {
                        i = 0;
                        return;
                    }
                    Console.getConsole().print("enter password again");
                    String pass2 = Console.getConsole().read();
                    if (pass1.equals(pass2)) {
                        player = new Player(s, pass1, System.nanoTime(), 30,
                                Models.mage, Models.mageDeck, Models.firstCards, Models.firstHeros, Models.firstDecks);
                        Console.getConsole().print(player.toString());
                        i = 1;
                        return;
                    } else {
                        Console.getConsole().print("password not same");
                        Console.getConsole().print("Enter again or Enter '&' to back");
                    }
                }
            } else {
                Console.getConsole().print("this user exist enter another");
                Console.getConsole().print("Enter again or Enter '&' to back");
            }

        }
    }

    private void welcome() {
        Console.getConsole().print("welcome");
    }


}
