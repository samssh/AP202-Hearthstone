package contoroller;

import model.Menu;
import model.Player;
import view.Console;

import java.util.ArrayList;

public class MenuController {
    private Menu menu;
    private Player player;

    MenuController(Menu menu, Player player) {
        this.menu = menu;
        this.player = player;
    }

    void run() {
        while (true) {
            action();
            Console console = Console.getConsole();
            if (menu.getMenuList().size() == 1) {
                menu = menu.getMenuList().get(0);
                continue;
            }
            while (true) {
                String s = console.read();
                for (Menu m : menu.getMenuList()) {
                    if (m.isHasEntryList()) {
                        if (m.getKey().length() <= s.length() &&
                                m.getKey().equals(s.substring(0, m.getKey().length()))) {
                            makeEntryList(m);
                            String k = s.substring(m.getKey().length());
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

    }
    private void makeEntryList(Menu m){
    }


}
