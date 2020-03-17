package hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.*;
import java.util.List;

public class Connector {
    private static final Connector connector = new Connector();
    private final SessionFactory sessionFactory = buildSessionFactory();
    private Session session;
    private boolean hasTransaction = false;

    private SessionFactory buildSessionFactory() {
        PrintStream err=System.err;
        try {
            PrintStream nullOut = new PrintStream(new File("./log/hibernate log.txt"));
            System.setErr(nullOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        System.setErr(err);
        return sessionFactory;
    }

    private Connector() {
    }

    public static Connector getConnector() {
        return connector;
    }

    public void open() {
        session = sessionFactory.openSession();
    }

    public void close() {
        if (session.isOpen())
            session.close();
    }

    public void beginTransaction() {
        if (!hasTransaction) {
            session.beginTransaction();
            hasTransaction = true;
        }
    }

    public void commit() {
        if (hasTransaction) {
            session.getTransaction().commit();
            hasTransaction = false;
        }
    }

    public void saveOrUpdate(Object o) {
        session.saveOrUpdate(o.getClass().getName(), o);
    }

    public void delete(Object o) {
        session.delete(o.getClass().getName(), o);
    }

    Object fetchById(Class c, Serializable id) {
        return session.get(c.getName(), id);
    }

    List fetchAll(Class c) {
        Criteria criteria = session.createCriteria(c.getName());
        return criteria.list();
    }

}