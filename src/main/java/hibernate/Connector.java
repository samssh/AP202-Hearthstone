package hibernate;

import util.ConfigFactory;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import util.Loop;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Connector {
    private final SessionFactory sessionFactory;
    private final List<SaveAble> save, delete, tempSave, tempDelete;
    private final Object lock;
    private final Loop worker;

    @SneakyThrows
    private SessionFactory buildSessionFactory(File file) {
        PrintStream err = System.err;
        File log = new File("./log");
        if (log.exists() || log.mkdirs()) {
            PrintStream logStream = new PrintStream(new File("./log/hibernate log.txt"));
            System.setErr(logStream);
        }
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure(file).build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        System.setErr(err);
        return sessionFactory;
    }

    public Connector(String configName) {
        sessionFactory = buildSessionFactory(ConfigFactory.getInstance().getConfigFile(configName));
        save = new ArrayList<>();
        delete = new ArrayList<>();
        tempDelete =new ArrayList<>();
        tempSave = new ArrayList<>();
        lock = new Object();
        worker = new Loop(30, this::persist);
        worker.start();
    }

    private void persist() {

        synchronized (lock) {
            tempSave.addAll(save);
            this.save.clear();
            tempDelete.addAll(delete);
            this.delete.clear();
        }
        if (tempSave.size() > 0 || tempDelete.size() > 0)
            synchronized (Connector.class) {
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                for (SaveAble saveAble : tempDelete) {
                    session.delete(saveAble);

                }
                tempDelete.clear();
                for (SaveAble saveAble : tempSave) {
                    session.saveOrUpdate(saveAble);
                }
                tempSave.clear();
                session.getTransaction().commit();
                session.close();
            }
    }

    public void close() {
        worker.stop();
    }

    public void save(SaveAble saveAble) {
        if (saveAble != null)
            synchronized (lock) {
                save.add(saveAble);
            }
    }

    public void delete(SaveAble saveAble) {
        if (saveAble != null)
            synchronized (lock) {
                delete.add(saveAble);
            }
    }

    public <E extends SaveAble> E fetch(Class<E> entity, Serializable id) {
        synchronized (Connector.class) {
            Session session = sessionFactory.openSession();
            E result = session.get(entity, id);
            session.close();
            return result;
        }
    }


    public <E extends SaveAble> List<E> fetchAll(Class<E> entity) {
        synchronized (Connector.class) {
            Session session = sessionFactory.openSession();
            List<E> result = session.createQuery("from " + entity.getName(), entity).getResultList();
            session.close();
            return result;
        }
    }

    public CriteriaBuilder getCriteriaBuilder() {
        synchronized (Connector.class) {
            Session session = sessionFactory.openSession();
            CriteriaBuilder result = session.getCriteriaBuilder();
            session.close();
            return result;
        }
    }

    public <E> TypedQuery<E> createQuery(CriteriaQuery<E> criteriaQuery) {
        synchronized (Connector.class) {
            Session session = sessionFactory.openSession();
            TypedQuery<E> result = session.createQuery(criteriaQuery);
            session.close();
            return result;
        }
    }

    public <E> CriteriaQuery<E> createCriteriaQuery(Class<E> entity) {
        synchronized (Connector.class) {
            Session session = sessionFactory.openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<E> result = criteriaBuilder.createQuery(entity);
            session.close();
            return result;
        }
    }

    public <E extends SaveAble> List<E> fetchWithRestriction(Class<E> entity, String fieldName, Object value) {
        synchronized (Connector.class) {
            Session session = sessionFactory.openSession();
            List<E> result = session.createQuery("from " + entity.getName() + " where " + fieldName
                    + "=" + "'" + value + "'", entity).getResultList();
            session.close();
            return result;
        }
    }
}