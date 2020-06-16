package ir.SAM.hearthstone.hibernate;

import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ir.SAM.hearthstone.util.Loop;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Connector {
    private final static Object staticLock = new Object();
    private final static PrintStream logFilePrintStream;

    static {
        PrintStream temp;
        String logPath = "./hibernate log" + File.separator + "hibernate log.txt";
        try {
            temp = new PrintStream(new File(logPath));
        } catch (FileNotFoundException e) {
            temp = System.err;
        }
        logFilePrintStream = temp;
    }

    private final SessionFactory sessionFactory;
    private final Set<SaveAble> save, delete, tempSave, tempDelete;
    private final Object lock;
    private final Loop worker;

    @SneakyThrows
    public Connector(File configFile, String password) {
        sessionFactory = buildSessionFactory(addPassword(getServiceRegistryBuilder(configFile), password).build());
        save = new HashSet<>();
        delete = new HashSet<>();
        tempDelete = new HashSet<>();
        tempSave = new HashSet<>();
        lock = new Object();
        worker = new Loop(30, this::persist);
        worker.start();
    }

    public Connector(File file) {
        this(file, null);
    }

    private SessionFactory buildSessionFactory(StandardServiceRegistry registry) {
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    private StandardServiceRegistryBuilder getServiceRegistryBuilder(File file) {
        PrintStream err = System.err;
        System.setErr(logFilePrintStream);
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder().configure(file);
        System.setErr(err);
        return registryBuilder;
    }

    private StandardServiceRegistryBuilder addPassword(StandardServiceRegistryBuilder registryBuilder, String password) {
        if (password != null) {
            PrintStream err = System.err;
            System.setErr(logFilePrintStream);
            registryBuilder.applySetting("hibernate.connection.password", password);
            System.setErr(err);
        }
        return registryBuilder;
    }

    private void persist() {
        synchronized (lock) {
            tempSave.addAll(save);
            this.save.clear();
            tempDelete.addAll(delete);
            this.delete.clear();
        }
        if (tempSave.size() > 0 || tempDelete.size() > 0)
            synchronized (staticLock) {
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
        synchronized (staticLock) {
            Session session = sessionFactory.openSession();
            List<E> result = session.createQuery("from " + entity.getName(), entity).getResultList();
            session.close();
            return result;
        }
    }

    public CriteriaBuilder getCriteriaBuilder() {
        synchronized (staticLock) {
            Session session = sessionFactory.openSession();
            CriteriaBuilder result = session.getCriteriaBuilder();
            session.close();
            return result;
        }
    }

    public <E> TypedQuery<E> createQuery(CriteriaQuery<E> criteriaQuery) {
        synchronized (staticLock) {
            Session session = sessionFactory.openSession();
            TypedQuery<E> result = session.createQuery(criteriaQuery);
            session.close();
            return result;
        }
    }

    public <E> CriteriaQuery<E> createCriteriaQuery(Class<E> entity) {
        synchronized (staticLock) {
            Session session = sessionFactory.openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<E> result = criteriaBuilder.createQuery(entity);
            session.close();
            return result;
        }
    }

    public <E extends SaveAble> List<E> fetchWithRestriction(Class<E> entity, String fieldName, Object value) {
        synchronized (staticLock) {
            Session session = sessionFactory.openSession();
            List<E> result = session.createQuery("from " + entity.getName() + " where " + fieldName
                    + "=" + "'" + value + "'", entity).getResultList();
            session.close();
            return result;
        }
    }
}