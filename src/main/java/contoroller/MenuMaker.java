package contoroller;

import model.Card;
import model.Hero;
import model.Menu;
import model.Models;

import java.util.ArrayList;
import java.util.List;


public class MenuMaker {
    public static Menu getWelcomeMenu(){
        Menu welcome=new Menu("welcome","",false);
        Menu signUp=new Menu("signUp","N",false);
        Menu login=new Menu("login","Y",false);
        Menu mainMenu=new Menu("main","",false);
        Menu logOut=new Menu("logOut","log Out",false);
        Menu deleteAcc=new Menu("deleteAccount","delete-player",false);
        Menu exit=new Menu("exit","exit-a",false);
        Menu helpMain=new Menu("helpMain","hearthstone--help",false);
        Menu store=new Menu("store","store",false);
        Menu buy=new Menu("buy","buy",true);
        Menu sell=new Menu("sell","sell",true);
        Menu wallet=new Menu("wallet","wallet",false);
        Menu buyAble=new Menu("buyAble","ls-b",false);
        Menu sellAble=new Menu("sellAble","ls-s",false);
        Menu helpCollection=new Menu("helpCollection","hearthstone--help",false);
        Menu helpStore=new Menu("helpStore","hearthstone--help",false);
        Menu collection=new Menu("collection","collections",false);
        Menu back=new Menu("back","back",false);
        Menu openHero=new Menu("openHero","ls -a -heros",false);
        Menu defaultHero=new Menu("defaultHero","ls -m -heros",false);
        Menu selectHero=new Menu("selectHero","select",true);
        Menu availableCard=new Menu("availableCard","ls -a -cards", false);
        Menu deckCards=new Menu("deckCards","ls -m -cards",false);
        Menu noDeckCard=new Menu("noDeckCard","ls -n -cards",false);
        Menu addCard=new Menu("addCard","add", true);
        Menu removeCard=new Menu("removeCard","remove", true);
        Menu detailStore=new Menu("detailStore","detail", true);
        Menu detailCollection=new Menu("detailCollection","detail", true);
        Menu allCard=new Menu("allCard","ls -o -card",false);
//        Menu =new Menu(,,false);

        welcome.getMenuList().add(signUp);
        welcome.getMenuList().add(login);
        welcome.getMenuList().add(exit);
        logOut.getMenuList().add(welcome);
        login.getMenuList().add(welcome);
        login.getMenuList().add(mainMenu);
        signUp.getMenuList().add(welcome);
        signUp.getMenuList().add(mainMenu);
        deleteAcc.getMenuList().add(welcome);
        mainMenu.getMenuList().add(logOut);
        mainMenu.getMenuList().add(deleteAcc);
        mainMenu.getMenuList().add(exit);
        mainMenu.getMenuList().add(helpMain);
        mainMenu.getMenuList().add(collection);
        mainMenu.getMenuList().add(store);
        helpMain.getMenuList().add(mainMenu);
        back.getMenuList().add(mainMenu);
        store.getMenuList().add(sellAble);
        store.getMenuList().add(sell);
        store.getMenuList().add(buyAble);
        store.getMenuList().add(buy);
        store.getMenuList().add(wallet);
        store.getMenuList().add(helpStore);
        store.getMenuList().add(back);
        store.getMenuList().add(detailStore);
        detailStore.getMenuList().add(store);
        buy.getMenuList().add(store);
        sell.getMenuList().add(store);
        sellAble.getMenuList().add(store);
        buyAble.getMenuList().add(store);
        wallet.getMenuList().add(store);
        helpStore.getMenuList().add(store);
        collection.getMenuList().add(helpCollection);
        collection.getMenuList().add(detailCollection);
        collection.getMenuList().add(removeCard);
        collection.getMenuList().add(allCard);
        collection.getMenuList().add(addCard);
        collection.getMenuList().add(back);
        collection.getMenuList().add(noDeckCard);
        collection.getMenuList().add(deckCards);
        collection.getMenuList().add(availableCard);
        collection.getMenuList().add(selectHero);
        collection.getMenuList().add(defaultHero);
        collection.getMenuList().add(openHero);
        helpCollection.getMenuList().add(collection);
        allCard.getMenuList().add(collection);
        detailCollection.getMenuList().add(collection);
        removeCard.getMenuList().add(collection);
        addCard.getMenuList().add(collection);
        noDeckCard.getMenuList().add(collection);
        deckCards.getMenuList().add(collection);
        availableCard.getMenuList().add(collection);
        selectHero.getMenuList().add(collection);
        defaultHero.getMenuList().add(collection);
        openHero.getMenuList().add(collection);
        List<String> heroName=new ArrayList<>();
        for (Hero H : Models.heroes)
            heroName.add(H.getName());
        List<String> cardName=new ArrayList<>();
        for (Card c : Models.cards)
            cardName.add(c.getName());
        buy.setEntryList(cardName);
        sell.setEntryList(cardName);
        addCard.setEntryList(cardName);
        removeCard.setEntryList(cardName);
        selectHero.setEntryList(heroName);
        detailCollection.setEntryList(new ArrayList<>());
        detailCollection.getEntryList().addAll(heroName);
        detailCollection.getEntryList().addAll(cardName);
        detailStore.setEntryList(new ArrayList<>());
        detailStore.getEntryList().addAll(heroName);
        detailStore.getEntryList().addAll(cardName);

//        .getMenuList().add();

        return welcome;
    }

//    public static void main(String[] args) throws FileNotFoundException {
//        File file=new File("lk.txt");
//        PrintStream p=new PrintStream(file);
//        getWelcomeMenu();
//        for (int i = 0; i < Menu.i; i++) {
//            p.println("case "+'"'+k[i].getName()+'"'+":");
//            p.println(k[i].getName()+"();");
//            p.println("break;");
//        }
//    }
}
