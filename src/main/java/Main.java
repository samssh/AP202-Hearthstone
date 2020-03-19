import controller.MenuController;
import controller.MenuMaker;
import controller.SamHandler;
import hibernate.Connector;
import model.Models;

import java.io.IOException;
import java.util.logging.*;

@SuppressWarnings("unchecked")
public class Main {
    private static final Logger logger =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static FileHandler fileTxt;
    public static SimpleFormatter formatter;
    public static void main(String[] args) throws IOException {
        // delete console handler
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
        logger.setLevel(Level.INFO);
        // add my handler
        logger.addHandler(SamHandler.getHandler());
        Connector connector=Connector.getConnector();
        connector.open();
        Models.load();
        MenuController.getMenuController().setMenu(MenuMaker.getWelcomeMenu());
        MenuController.getMenuController().run();
        connector.close();
        System.exit(-1);
    }
}
