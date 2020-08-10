package ir.sam.hearthstone.server.util.hibernate;

import ir.sam.hearthstone.server.util.Loop;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Connector {
    private final static Object staticLock = new Object();
    private final static PrintStream logFilePrintStream;
    private Throwable lastThrowable;

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
    private final Set<SaveAble> save, delete;
    private final Object lock;
    private final Loop worker;

    @SneakyThrows
    public Connector(File configFile, String password) {
        sessionFactory = buildSessionFactory(addPassword(getServiceRegistryBuilder(configFile), password).build());
        save = new HashSet<>();
        delete = new HashSet<>();
        lock = new Object();
        worker = new Loop(30, this::persist);
        worker.start();
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

    private void ensureOpen() throws DatabaseDisconnectException {
        if (lastThrowable != null)
            throw new DatabaseDisconnectException(lastThrowable);
    }

    private void persist() {
        Set<SaveAble> tempDelete;
        Set<SaveAble> tempSave;
        synchronized (lock) {
            tempSave = new HashSet<>(save);
            this.save.clear();
            tempDelete = new HashSet<>(delete);
            this.delete.clear();
        }
        if (tempSave.size() > 0 || tempDelete.size() > 0)
            synchronized (staticLock) {
                Session session = sessionFactory.openSession();
                try {
                    session.beginTransaction();
                } catch (Throwable e) {
                    lastThrowable = e;
                    e.printStackTrace();
                    return;
                }
                for (SaveAble saveAble : tempDelete) {
                    try {
                        session.delete(saveAble);
                    } catch (Throwable e) {
                        lastThrowable = e;
                        e.printStackTrace();
                        System.err.println("instance not deleted: " + saveAble);
                        return;
                    }
                }
                for (SaveAble saveAble : tempSave) {
                    try {
                        session.saveOrUpdate(saveAble);
                    } catch (Throwable e) {
                        lastThrowable = e;
                        e.printStackTrace();
                        System.err.println("instance not saved :" + saveAble);
                        return;
                    }
                }
                try {
                    session.getTransaction().commit();
                } catch (Throwable e) {
                    lastThrowable = e;
                    e.printStackTrace();
                    System.err.println("instances not saved :" + tempSave);
                    System.err.println("instances not deleted :" + tempDelete);
                }
                tempSave.clear();
                tempDelete.clear();
                session.close();
            }
    }

    public void close() {
        worker.stop();
        sessionFactory.close();
    }

    public void save(SaveAble saveAble) throws DatabaseDisconnectException {
        if (saveAble != null)
            synchronized (lock) {
                ensureOpen();
                save.add(saveAble);
            }
    }

    public void delete(SaveAble saveAble) throws DatabaseDisconnectException {
        if (saveAble != null)
            synchronized (lock) {
                ensureOpen();
                delete.add(saveAble);
            }
    }

    public <E extends SaveAble> E fetch(Class<E> entity, Serializable id) throws DatabaseDisconnectException {
        synchronized (staticLock) {
            ensureOpen();
            Session session = sessionFactory.openSession();
            E result = null;
            try {
                result = session.get(entity, id);
            } catch (Throwable e) {
                lastThrowable = e;
                e.printStackTrace();
                throw new DatabaseDisconnectException(e);
            }
            session.close();
            return result;
        }
    }

    public <E extends SaveAble> List<E> fetchAll(Class<E> entity) throws DatabaseDisconnectException {
        String hql = "FROM " + entity.getName();
        return executeHQL(hql, entity);
    }

    public <E extends SaveAble> List<E> executeHQL(String hql, Class<E> entity) throws DatabaseDisconnectException {
        synchronized (staticLock) {
            ensureOpen();
            Session session = sessionFactory.openSession();
            List<E> result = null;
            try {
                result = session.createQuery(hql, entity).getResultList();
            } catch (Throwable e) {
                lastThrowable = e;
                e.printStackTrace();
                throw new DatabaseDisconnectException(e);
            }
            session.close();
            return result;
        }
    }

    public <E extends SaveAble> List<E> executeSQLQuery(String sql, Class<E> entity) throws DatabaseDisconnectException {
        synchronized (staticLock) {
            ensureOpen();
            Session session = sessionFactory.openSession();
            List<E> result = null;
            try {
                result = session.createNativeQuery(sql, entity).getResultList();
            } catch (Exception e) {
                lastThrowable = e;
                e.printStackTrace();
                throw new DatabaseDisconnectException(e);
            }
            session.close();
            return result;
        }
    }

    public <E extends SaveAble> List<E> fetchWithRestriction(Class<E> entity, String fieldName, Object value) throws DatabaseDisconnectException {
        String hql = "from " + entity.getName() + " where " + fieldName + "=" + "'" + value + "'";
        return executeHQL(hql, entity);
    }
}