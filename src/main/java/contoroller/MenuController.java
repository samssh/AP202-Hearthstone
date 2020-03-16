package contoroller;

import hibernate.Connector;
import model.*;
import view.Console;

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
            while (true) {
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
                            break;
                        }

                    } else {
                        if (m.getKey().equals(s)) {
                            menu = m;
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
            case "allCard":
                allCard();
                break;
        }


    }

    private void buyAble() {

    }

    private void wallet() {
        Console.getConsole().print("your coin is: "+player.getCoin());
        i=0;
    }

    private void sell() {
        Card c=Models.search(menu.getEntry());
        if (player.getCards().contains(c)) {
            if (!player.isInDeck(c)){
                player.removeCard(c);
                player.setCoin(player.getCoin()+c.getPrice());
                Console.getConsole().print("sell card done");
            }else {
                Console.getConsole().print("you cant sell this card because this card in your deck");
            }
        }else {
            Console.getConsole().print("you dont have this card");
        }
        i=0;
    }

    private void buy() {
        Card c=Models.search(menu.getEntry());
        if (c.getPrice()<=player.getCoin()) {
            if (player.numberOfCard(c) < 2){
                player.addCard(c);
                player.setCoin(player.getCoin()-c.getPrice());
                Console.getConsole().print("buy card done");
            }else {
                Console.getConsole().print("you cant buy this because you have 2 of this");
            }
        }else {
            Console.getConsole().print("you dont have enough coin to buy this");
        }
        i=0;
    }

    private void store() {
        Console.getConsole().print("Store");
    }

    private void helpMain() {
        // keys most be printed
        Console.getConsole().print("help");
        i=0;
    }

    private void exit() {
        Connector connector=Connector.getConnector();
        connector.open();
        connector.beginTransaction();
        if (player!=null)
        player.saveOrUpdate();
        connector.commit();
        connector.close();
        System.exit(0);
    }

    private void deleteAccount() {
        Connector connector=Connector.getConnector();
        connector.open();
        connector.beginTransaction();
        player.delete();
        player=null;
        connector.commit();
        connector.close();
        i=0;
    }

    private void logOut() {
        Connector connector=Connector.getConnector();
        connector.open();
        connector.beginTransaction();
        player.saveOrUpdate();
        player=null;
        connector.commit();
        connector.close();
        i=0;
    }

    private void main() {
        Console.getConsole().print("main menu");
    }

    private void login() {
        while (true) {
            Console.getConsole().print("enter user name");
            String s = Console.getConsole().read();
            if ("&".equals(s)){
                i=0;
                return;
            }
            Player p=(Player) Connector.getConnector().fetchById(Player.class,s);
            if (p!=null){
                while (true) {
                    Console.getConsole().print("enter password");
                    String pass1 = Console.getConsole().read();
                    if ("&".equals(pass1)){
                        i=0;
                        return;
                    }
                    if (pass1.equals(p.getPassword())){
                        player=p;
                        i=1;
                        return;
                    }else {
                        Console.getConsole().print("password wrong");
                        Console.getConsole().print("Enter again or Enter '&' to back");
                    }
                }
            }else {
                Console.getConsole().print("this user not exist enter another");
                Console.getConsole().print("Enter again or Enter '&' to back");
            }
        }
    }

    private void signUp() {
        while (true) {
            Console.getConsole().print("enter user name");
            String s = Console.getConsole().read();
            if ("&".equals(s)){
                i=0;
                return;
            }
            Player p=(Player) Connector.getConnector().fetchById(Player.class,s);
            if (p==null){
                while (true) {
                    Console.getConsole().print("enter password");
                    String pass1 = Console.getConsole().read();
                    if ("&".equals(pass1)){
                        i=0;
                        return;
                    }
                    Console.getConsole().print("enter password again");
                    String pass2 = Console.getConsole().read();
                    if (pass1.equals(pass2)){
                        player=new Player(s,pass1,System.nanoTime(),30,
                                Models.mage,Models.firstCards,Models.firstHeros,Models.firstDecks);
                        i=1;
                        return;
                    }else {
                        Console.getConsole().print("password not same");
                        Console.getConsole().print("Enter again or Enter '&' to back");
                    }
                }
            }else {
                Console.getConsole().print("this user exist enter another");
                Console.getConsole().print("Enter again or Enter '&' to back");
            }

        }
    }

    private void welcome() {
        Console.getConsole().print("welcome");
    }


}
