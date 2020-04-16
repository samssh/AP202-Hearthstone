package hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.*;
import java.util.List;

public class Connector {
    private static final Connector connector = new Connector();
    private final SessionFactory sessionFactory = buildSessionFactory();
    private Session session;
    private boolean hasTransaction = false;

    private SessionFactory buildSessionFactory() {
        PrintStream err = System.err;
        try {
            PrintStream nullOut = new PrintStream(new File("./log/hibernate log.txt"));
//            System.setErr(nullOut);
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
            session.close();
            session = sessionFactory.openSession();
            hasTransaction = false;
        }
    }

    public void saveOrUpdate(SaveAble saveAble) {
        session.saveOrUpdate(saveAble);
    }

    public void delete(SaveAble saveAble) {
        session.delete(saveAble);
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return session.getCriteriaBuilder();
    }

    public <E> TypedQuery<E> createQuery(CriteriaQuery<E> criteriaQuery) {
        return session.createQuery(criteriaQuery);
    }

    public <E> CriteriaQuery<E> createCriteriaQuery(Class<E> entity) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        return criteriaBuilder.createQuery(entity);
    }

    public <E extends SaveAble> E fetch(Class<E> entity, Serializable id) {
        return session.get(entity, id);
    }

    public <E extends SaveAble> List<E> fetchAll(Class<E> entity) {
        return session.createQuery("from " + entity.getName(), entity).getResultList();
    }

    public <E extends SaveAble> List<E> fetchWithRestriction(Class<E> entity, String fieldName, Object value) {
        return session.createQuery("from " + entity.getName() + " where " + fieldName
                + "=" + "'" + value + "'", entity).getResultList();
    }
}