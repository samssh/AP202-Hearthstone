package view;

import model.Menu;

public class MenuView {
    public void view(Menu menu){
        switch (menu.getName()) {
            case "welcome":
                Console.getConsole().print(ConsoleColors.BLUE+"welcome");
                break;
            case "signUp":
                Console.getConsole().print(ConsoleColors.BLUE+"signUp");
                break;
            case "login":
                Console.getConsole().print(ConsoleColors.BLUE+"login");
                break;
            case "main":
                Console.getConsole().print(ConsoleColors.BLUE+"main");
                break;
            case "logOut":
                Console.getConsole().print(ConsoleColors.BLUE+"logOut");
                break;
            case "deleteAccount":
                Console.getConsole().print(ConsoleColors.BLUE+"deleteAccount");
                break;
            case "exit":
                Console.getConsole().print(ConsoleColors.BLUE+"exit");
                break;
            case "helpMain":
                Console.getConsole().print(ConsoleColors.BLUE+"helpMain");
                break;
            case "store":
                Console.getConsole().print(ConsoleColors.BLUE+"store");
                break;
            case "buy":
                Console.getConsole().print(ConsoleColors.BLUE+"buy");
                break;
            case "sell":
                Console.getConsole().print(ConsoleColors.BLUE+"sell");
                break;
            case "wallet":
                Console.getConsole().print(ConsoleColors.BLUE+"wallet");
                break;
            case "buyAble":
                Console.getConsole().print(ConsoleColors.BLUE+"buyAble");
                break;
            case "sellAble":
                Console.getConsole().print(ConsoleColors.BLUE+"sellAble");
                break;
            case "helpCollection":
                Console.getConsole().print(ConsoleColors.BLUE+"helpCollection");
                break;
            case "helpStore":
                Console.getConsole().print(ConsoleColors.BLUE+"helpStore");
                break;
            case "collection":
                Console.getConsole().print(ConsoleColors.BLUE+"collection");
                break;
            case "back":
                Console.getConsole().print(ConsoleColors.BLUE+"back");
                break;
            case "openHero":
                Console.getConsole().print(ConsoleColors.BLUE+"openHero");
                break;
            case "defaultHero":
                Console.getConsole().print(ConsoleColors.BLUE+"defaultHero");
                break;
            case "selectHero":
                Console.getConsole().print(ConsoleColors.BLUE+"selectHero");
                break;
            case "availableCard":
                Console.getConsole().print(ConsoleColors.BLUE+"availableCard");
                break;
            case "deckCards":
                Console.getConsole().print(ConsoleColors.BLUE+"deckCards");
                break;
            case "noDeckCard":
                Console.getConsole().print(ConsoleColors.BLUE+"noDeckCard");
                break;
            case "addCard":
                Console.getConsole().print(ConsoleColors.BLUE+"addCard");
                break;
            case "removeCard":
                Console.getConsole().print(ConsoleColors.BLUE+"removeCard");
                break;
            case "detailStore":
                Console.getConsole().print(ConsoleColors.BLUE+"detailStore");
                break;
            case "detailCollection":
                Console.getConsole().print(ConsoleColors.BLUE+"detailCollection");
                break;
            case "allCard":
                Console.getConsole().print(ConsoleColors.BLUE+"allCard");
                break;
            default:
                System.err.println("ride man "+menu.getName());
        }

    }
}
