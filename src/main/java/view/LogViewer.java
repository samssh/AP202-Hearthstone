package view;

import hibernate.Connector;
import hibernate.ManualMapping;
import hibernate.SaveAble;
import model.BodyLog;
import model.HeaderLog;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class LogViewer {
    public static void main(String[] args) {
        Connector connector = Connector.getConnector();
        Scanner in = new Scanner(System.in);
        connector.open();
        System.out.println("enter user name");
        String userName = in.next();
        Criteria criteriaHeader = connector.createCriteria(HeaderLog.class);
        List<HeaderLog> headerLogs = criteriaHeader.add(Restrictions.eq("userName", userName)).list();
        headerLogs.forEach(System.out::println);
        System.out.println("now enter id:");
        long id=in.nextLong();
        in.nextLine();
        System.out.println("now enter time of log(enter time distance until now to hours)");
        Long hour=in.nextLong();
        in.nextLine();
        String end=new Date(System.currentTimeMillis()-hour*3600000).toInstant().toString();
        String start=new Date(System.currentTimeMillis()-(hour+1)*3600000).toInstant().toString();
        Criteria criteriaBody=connector.createCriteria(BodyLog.class);
        criteriaBody.add(Restrictions.between("instant",start,end));
        criteriaBody.add(Restrictions.eq("headId",id));
        List<BodyLog> bodyLogs=criteriaBody.list();
        System.out.println();
        System.out.println();
        bodyLogs.forEach(System.out::println);
        System.exit(0);
    }
}
