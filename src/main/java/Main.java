import contoroller.MenuController;
import contoroller.MenuMaker;
import hibernate.Connector;
import model.Models;

public class Main {
    public static void main(String[] args) {
        Connector connector=Connector.getConnector();
        connector.open();
        Models.load();
        connector.close();
        new MenuController(MenuMaker.getWelcomeMenu()).run();
        System.exit(-1);
    }
}
