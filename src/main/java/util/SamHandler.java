//package controller;
//
//import hibernate.Connector;
//import lombok.Getter;
//import model.BodyLog;
//
//import java.util.logging.Handler;
//import java.util.logging.LogRecord;
//
//public class SamHandler extends Handler {
//    @Getter
//    private static SamHandler handler=new SamHandler();
//    private SamHandler(){
//        super();
//    }
//
//    @Override
//    public void publish(LogRecord logRecord) {
//        BodyLog bodyLog=new BodyLog(logRecord);
//        bodyLog.setHeadId(MenuController.getMenuController().getPlayer().getCreatTime());
//        Connector.getInstance().beginTransaction();
//        bodyLog.saveOrUpdate();
//        Connector.getInstance().commit();
//    }
//
//    @Override
//    public void flush() {}
//
//    @Override
//    public void close() throws SecurityException {}
//}
