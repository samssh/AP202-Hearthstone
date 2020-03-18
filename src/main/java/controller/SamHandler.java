package controller;

import hibernate.Connector;
import model.BodyLog;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SamHandler extends Handler {
    private static SamHandler samHandler=new SamHandler();
    private SamHandler(){
        super();
    }

    public static SamHandler getHandler() {
        return samHandler;
    }

    @Override
    public void publish(LogRecord logRecord) {
        BodyLog bodyLog=new BodyLog(logRecord);
        bodyLog.setHeadId(MenuController.getMenuController().getPlayer().getCreatTime());
        Connector.getConnector().beginTransaction();
        bodyLog.saveOrUpdate();
        Connector.getConnector().commit();
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}
}
