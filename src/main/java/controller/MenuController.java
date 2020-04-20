//package controller;
//
//import hibernate.Connector;
//import lombok.Getter;
//import model.*;
//import view.Console;
//import view.Menu;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//
//public class MenuController {
//    @Getter
//    private static final MenuController menuController = new MenuController();
//
//    private MenuController() {
//    }
//
//    private Menu menu;
//    private Player player;
//    private int i = -1;
//
//    public void setMenu(Menu menu) {
//        this.menu = menu;
//    }
//
//    Player getPlayer() {
//        return player;
//    }
//
//    public void run() {
//        while (true) {
//            action();
//            Console console = Console.getConsole();
//            if (i >= 0) {
//                menu = menu.getMenuList().get(i);
//                i = -1;
//                continue;
//            }
//            boolean end = true;
//            while (end) {
//                boolean wrong = true;
//                String s = console.read().trim();
//                for (Menu m : menu.getMenuList()) {
//                    if (m.isHasEntryList()) {
//                        if (m.getKey().length() <= s.length() &&
//                                m.getKey().equals(s.substring(0, m.getKey().length()))) {
//                            String k = s.substring(m.getKey().length()).trim();
//                            for (String t : m.getEntryList()) {
//                                if (t.equals(k)) {
//                                    menu = m;
//                                    m.setEntry(k);
//                                    wrong = false;
//                                    break;
//                                }
//                            }
//                            end = false;
//                            break;
//                        }
//
//                    } else {
//                        if (m.getKey().equals(s)) {
//                            menu = m;
//                            wrong = false;
//                            end = false;
//                            break;
//                        }
//                    }
//                }
//                if (wrong) {
//                    Console.getConsole().print("wrong input");
//                }
//                //incorrect input state
//            }
//        }
//    }
//
//    private void action() {
//        Class<MenuController> menuControllerClass = MenuController.class;
//        Method method;
//        try {
//            method = menuControllerClass.getDeclaredMethod(menu.getName());
//            method.invoke(this);
//        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void detailCollection() {
//        try {
//            Console.getConsole().print(Objects.requireNonNull(ModelLoader.searchUnit(menu.getEntry())).toString());
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//        i = 0;
//    }
//
//    private void detailStore() {
//        try {
//            Console.getConsole().print(Objects.requireNonNull(ModelLoader.searchUnit(menu.getEntry())).toString());
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//
//        }
//        i = 0;
//    }
//
//    private void removeCard() {
//        Card card = ModelLoader.searchCard(menu.getEntry());
//        if (card == null) {
//            i = 0;
//            return;
//        }
//        if (player.getCards().contains(card)) {
//            if (player.getSelectedDeck().numberOfCard(card) > 0) {
//                player.getSelectedDeck().removeCard(card);
//                Console.getConsole().print("card removed");
//            } else {
//                Console.getConsole().print("this card not in deck");
//            }
//        } else {
//            Console.getConsole().print("you dont have this card");
//        }
//        i = 0;
//    }
//
//    private void addCard() {
//        Card card = ModelLoader.searchCard(menu.getEntry());
//        if (card == null) {
//            i = 0;
//            return;
//        }
//        if (player.getCards().contains(card)) {
//            if (card.getClassOfCard().isItForHero(player.getSelectedHero())
//                    && player.getSelectedDeck().numberOfCard(card) < 2) {
//                if (player.getSelectedDeck().getCardList().size() < 15) {
//                    player.getSelectedDeck().addCard(card);
//                    Console.getConsole().print("card added");
//                } else {
//                    Console.getConsole().print("your deck is full");
//                }
//            } else {
//                Console.getConsole().print("you cant add this card to your deck");
//            }
//        } else {
//            Console.getConsole().print("you dont have this card");
//        }
//        i = 0;
//    }
//
//    private void allCard() {
//        List<String> list = new ArrayList<>();
//        List<Card> cards = player.getCards();
//        for (int i = 0; i < cards.size(); i++) {
//            Card c = cards.get(i);
//            if (i < cards.size() - 1 && c.equals(cards.get(i + 1))) {
//                list.add(c.getName() + "(2)");
//                i++;
//                continue;
//            }
//            list.add(c.getName());
//        }
//        if (list.size() == 0)
//            Console.getConsole().print("no card for you");
//        else {
//            Console.getConsole().print("the card for you");
//            for (String str : list)
//                Console.getConsole().print(str);
//        }
//        i = 0;
//    }
//
//    private void noDeckCard() {
//        List<String> list = new ArrayList<>();
//        List<Card> cards = player.getCards();
//        for (int i = 0; i < cards.size(); i++) {
//            Card c = cards.get(i);
//            if (c.getClassOfCard().isItForHero(player.getSelectedHero())
//                    && player.getSelectedDeck().numberOfCard(c) < 2) {
//                list.add(c.getName());
//                if (i < cards.size() - 1 && c.equals(cards.get(i + 1))) {
//                    i++;
//                }
//
//            }
//        }
//        if (list.size() == 0)
//            Console.getConsole().print("no card to add");
//        else {
//            Console.getConsole().print("the card that you can add to your deck:");
//            for (String str : list)
//                Console.getConsole().print(str);
//        }
//        i = 0;
//    }
//
//    private void deckCards() {
//        List<String> list = new ArrayList<>();
//        List<Card> cList = player.getSelectedDeck().getCardList();
//        for (int i = 0; i < cList.size(); i++) {
//            if (i < cList.size() - 1) {
//                if (cList.get(i).equals(cList.get(i + 1))) {
//                    list.add(cList.get(i).getName() + "(2)");
//                    i++;
//                    continue;
//                }
//            }
//            list.add(cList.get(i).getName());
//        }
//        if (list.size() == 0)
//            Console.getConsole().print("no card in Deck");
//        else {
//            Console.getConsole().print("the card in your deck:");
//            for (String str : list)
//                Console.getConsole().print(str);
//        }
//        i = 0;
//    }
//
//    private void availableCard() {
//        List<String> list = new ArrayList<>();
//        List<Card> cards = player.getCards();
//        for (int i = 0; i < cards.size(); i++) {
//            Card c = cards.get(i);
//            if (c.getClassOfCard().isItForHero(player.getSelectedHero())) {
//                if (i < cards.size() - 1 && c.equals(cards.get(i + 1))) {
//                    list.add(c.getName() + "(2)");
//                    i++;
//                    continue;
//                }
//                list.add(c.getName());
//            }
//        }
//        if (list.size() == 0)
//            Console.getConsole().print("no card for your hero");
//        else {
//            Console.getConsole().print("the card for your hero:");
//            for (String str : list)
//                Console.getConsole().print(str);
//        }
//        i = 0;
//    }
//
//    private void selectHero() {
//        Hero hero = ModelLoader.searchHero(menu.getEntry());
//        if (hero == null) {
//            i = 0;
//            return;
//        }
//        for (Hero h : player.getHeroes())
//            if (h.equals(hero)) {
//                if (!h.equals(player.getSelectedHero())) {
//                    player.setSelectedDeckIndex(player.getHeroDeckIndex(h));
//                    Console.getConsole().print("your default hero has changed to " + h.getName());
//                } else {
//                    Console.getConsole().print("your hero already is " + h.getName());
//                }
//                i = 0;
//                return;
//            }
//        Console.getConsole().print("this hero is locked");
//        i = 0;
//    }
//
//    private void defaultHero() {
//        Console.getConsole().print("your default hero is:");
//        Console.getConsole().print(player.getSelectedHero().getName());
//        i = 0;
//    }
//
//    private void openHero() {
//        Console.getConsole().print("your hero is:");
//        for (Hero h : player.getHeroes())
//            Console.getConsole().print(h.getName());
//        i = 0;
//    }
//
//    private void back() {
//        i = 0;
//    }
//
//    private void collection() {
//        Console.getConsole().print("Collection");
//        Console.getConsole().print("enter help to show commands");
//    }
//
//    private void helpStore() {
//        Console.getConsole().print("help");
//        Console.getConsole().print("enter 'buy [card name]' to buy card");
//        Console.getConsole().print("enter 'sell [card name]' to sell card");
//        Console.getConsole().print("enter 'ls -b' to see all cards you can buy");
//        Console.getConsole().print("enter 'ls -s' to see all cards you can sell");
//        Console.getConsole().print("enter 'wallet' to see wallet");
//        Console.getConsole().print("enter 'detail [card or hero name]' to see detail");
//        Console.getConsole().print("enter 'back' to back");
//        i = 0;
//    }
//
//    private void helpCollection() {
//        Console.getConsole().print("help");
//        Console.getConsole().print("enter 'select [hero name]' to select hero");
//        Console.getConsole().print("enter 'ls -a -heros' to to see all heros");
//        Console.getConsole().print("enter 'ls -m -heros' to to see selected hero");
//        Console.getConsole().print("enter 'add [card name]' to add card to your deck");
//        Console.getConsole().print("enter 'remove [card name]' to remove card from tour deck");
//        Console.getConsole().print("enter 'ls -a -cards' to see all Neutral card and " + player.getSelectedHero().getName() + "card");
//        Console.getConsole().print("enter 'ls -m -cards' to see cards in deck");
//        Console.getConsole().print("enter 'ls -n -cards' to see all card can add in deck");
//        Console.getConsole().print("enter 'ls -o -cards' to see all of your card");
//        Console.getConsole().print("enter 'detail [card or hero name]' to see detail");
//        Console.getConsole().print("enter 'back' to back");
//        i = 0;
//    }
//
//    private void sellAble() {
//        List<String> list = new ArrayList<>();
//        for (Card c : player.getCards()) {
//            if (!player.isInDeck(c) && player.getCoin() >= c.getPrice())
//                list.add(c.getName());
//        }
//        if (list.size() == 0)
//            Console.getConsole().print("nothing to sell!!!");
//        else {
//            Console.getConsole().print("you can sell these:");
//            for (String str : list) {
//                Console.getConsole().print(str);
//            }
//        }
//        i = 0;
//    }
//
//    private void buyAble() {
//        List<String> list = new ArrayList<>();
//        for (Card c : ModelLoader.cards) {
//            if (c.getPrice() <= player.getCoin() && player.numberOfCard(c) < 1)
//                list.add(c.getName());
//        }
//        if (list.size() == 0)
//            Console.getConsole().print("nothing to buy!!!");
//        else {
//            Console.getConsole().print("you can buy these:");
//            for (String str : list) {
//                Console.getConsole().print(str);
//            }
//        }
//        i = 0;
//    }
//
//    private void wallet() {
//        Console.getConsole().print("your coin is: " + player.getCoin());
//        i = 0;
//    }
//
//    private void sell() {
//        Card card = ModelLoader.searchCard(menu.getEntry());
//        if (card == null) {
//            i = 0;
//            return;
//        }
//        if (player.getCards().contains(card)) {
//            if (!player.isInDeck(card)) {
//                player.removeCard(card);
//                player.setCoin(player.getCoin() + card.getPrice());
//                Console.getConsole().print("sell card done");
//            } else {
//                Console.getConsole().print("you cant sell this card because this card in your deck");
//            }
//        } else {
//            Console.getConsole().print("you dont have this card");
//
//        }
//        i = 0;
//    }
//
//    private void buy() {
//        Card card = ModelLoader.searchCard(menu.getEntry());
//        if (card == null) {
//            i = 0;
//            return;
//        }
//        if (card.getPrice() <= player.getCoin()) {
//            if (player.numberOfCard(card) < 1) {
//                player.addCard(card);
//                player.setCoin(player.getCoin() - card.getPrice());
//                Console.getConsole().print("buy card done");
//            } else {
//                Console.getConsole().print("you cant buy this because you have this card");
//            }
//        } else {
//            Console.getConsole().print("you dont have enough coin to buy this");
//        }
//        i = 0;
//    }
//
//    private void store() {
//        Console.getConsole().print("Store");
//        Console.getConsole().print("enter help to show commands");
//    }
//
//    private void helpMain() {
//        Console.getConsole().print("enter 'collection' to go to the collection");
//        Console.getConsole().print("enter 'store' to go to the store");
//        Console.getConsole().print("enter 'delete account' to delete account");
//        Console.getConsole().print("enter 'log out' to delete log out");
//        Console.getConsole().print("enter 'exit' to exit");
//        i = 0;
//    }
//
//    private void exit() {
//        Connector connector = Connector.getInstance();
//        connector.beginTransaction();
//        if (player != null)
//            player.saveOrUpdate();
//        connector.commit();
//        connector.close();
//        System.exit(0);
//    }
//
//    private void deleteAccount() {
//        Console.getConsole().print("enter password");
//        while (true) {
//            String s = Console.getConsole().read();
//            if (player.getPassword().equals(s))
//                break;
//            else {
//                Console.getConsole().print("wrong password enter again or & to back");
//            }
//            if ("&".equals(s)) {
//                i = 1;
//                return;
//            }
//        }
//        Connector connector = Connector.getInstance();
//        HeaderLog headerLog = Connector.getInstance().fetch(HeaderLog.class, player.getCreatTime());
//        headerLog.setDeletedAt(new Date(System.currentTimeMillis()).toString());
//        connector.beginTransaction();
//        headerLog.saveOrUpdate();
//        player.delete();
//        player = null;
//        connector.commit();
//        i = 0;
//    }
//
//    private void logOut() {
//        Connector connector = Connector.getInstance();
//        connector.beginTransaction();
//        player.saveOrUpdate();
//        connector.commit();
//        player = null;
//        i = 0;
//    }
//
//    private void main() {
//        Console.getConsole().print("main menu");
//        Console.getConsole().print("enter help to show commands");
//    }
//
//
//    private void welcome() {
//        Console.getConsole().print("welcome");
//        Console.getConsole().print("already have an account?");
//        Console.getConsole().print("if you have an account just enter y");
//        Console.getConsole().print("if dont you have an account just enter n");
//        Console.getConsole().print("enter 'exit' to exit");
//    }
//}
