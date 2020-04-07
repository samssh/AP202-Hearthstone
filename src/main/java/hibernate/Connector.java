package hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    public void saveOrUpdate(SaveAble o) {
        session.saveOrUpdate(o);
    }

    public void delete(SaveAble o) {
        session.delete(o);
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

    public <E extends SaveAble, T extends Serializable> void saveOrUpdateList(List<T> listId, List<E> objectList) {
        listId.subList(0, listId.size()).clear();
        for (SaveAble element : objectList) {
            element.saveOrUpdate();
            listId.add(element.getId());
        }
    }

    public <E extends SaveAble> void deleteList(List<E> objectList) {
        for (SaveAble o : objectList) o.delete();
    }

    public <E extends SaveAble, T extends Serializable> void fetchList(Class<E> entity, List<T> listId, List<E> objectList) {
        objectList.subList(0, objectList.size()).clear();
        for (Serializable s : listId) {
            objectList.add(fetch(entity, s));
        }
    }

    public <E extends SaveAble> E fetch(Class<E> entity, Serializable id) {
        E o = session.get(entity, id);
        if (o == null)
            return null;
        o.load();
        return o;
    }

    public <E extends SaveAble> List<E> fetchAll(Class<E> entity) {
        CriteriaQuery<E> criteriaQuery = createCriteriaQuery(entity);
        Root<E> eRoot = criteriaQuery.from(entity);
        criteriaQuery.select(eRoot);
        TypedQuery<E> typedQuery = createQuery(criteriaQuery);
        List<E> list = typedQuery.getResultList();
        for (SaveAble o : list) {
            o.load();
        }
        return list;
    }

    public <E extends SaveAble> List<E> fetchWithRestriction(Class<E> entity, String fieldName, Object value) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entity);
        Root<E> eRoot = criteriaQuery.from(entity);
        criteriaQuery.select(eRoot);
        criteriaQuery.where(criteriaBuilder.equal(eRoot.get(fieldName), value));
        TypedQuery<E> typedQuery = session.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}