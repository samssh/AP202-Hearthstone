package view;

import hibernate.Connector;
import model.BodyLog;
import model.HeaderLog;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class LogViewer {
    public static void main(String[] args) {
        Connector connector = Connector.getConnector();
        Scanner in = new Scanner(System.in);
        connector.open();
        System.out.println("enter user name");
        String userName = in.next();
        List<HeaderLog> headerLogs=connector.fetchWithRestriction(HeaderLog.class,"userName",userName);
        headerLogs.forEach(System.out::println);
        System.out.println("now enter id:");
        long id=in.nextLong();
        in.nextLine();
        System.out.println("now enter time of log(enter time distance until now to hours)");
        long hour=in.nextLong();
        in.nextLine();
        String end=new Date(System.currentTimeMillis()-hour*3600000).toInstant().toString();
        String start=new Date(System.currentTimeMillis()-(hour+1)*3600000).toInstant().toString();
        CriteriaBuilder criteriaBuilder=connector.getCriteriaBuilder();
        CriteriaQuery<BodyLog> bodyLogCriteriaQuery= connector.createCriteriaQuery(BodyLog.class);
        Root<BodyLog> bodyLogRoot=bodyLogCriteriaQuery.from(BodyLog.class);
        bodyLogCriteriaQuery.select(bodyLogRoot);
        bodyLogCriteriaQuery.where(criteriaBuilder.equal(bodyLogRoot.get("headId"),id))
                .where(criteriaBuilder.between(bodyLogRoot.get("instant"),start,end));
        TypedQuery<BodyLog> bodyLogTypedQuery=connector.createQuery(bodyLogCriteriaQuery);
        List<BodyLog> bodyLogs=bodyLogTypedQuery.getResultList();
        System.out.println();
        System.out.println();
        bodyLogs.forEach(System.out::println);
        System.exit(0);
    }
}
