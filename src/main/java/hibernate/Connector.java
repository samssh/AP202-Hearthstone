package hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.script.ScriptEngine;
import java.util.ArrayList;
import java.util.List;

public class Connector{
    private static final Connector connector= new Connector();
    private final SessionFactory sessionFactory = buildSessionFactory();
    private Session session;
    private boolean hasTransaction=false;

    private SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
    private Connector(){
    }
    public static Connector getConnector() {
        return connector;
    }
    public void open(){
        session=sessionFactory.openSession();
    }
    public void close() {
        if (session.isOpen())
            session.close();
    }
    public void beginTransaction(){
        if (!hasTransaction){
            session.beginTransaction();
            hasTransaction=true;
        }
    }
    public void commit(){
        if (hasTransaction) {
            session.getTransaction().commit();
            hasTransaction = false;
        }
    }
    public Integer save(Object o){
        return (Integer) session.save(o.getClass().getName(),o);
    }
    public void update(Object o){
        session.update(o.getClass().getName(),o);
    }
    public void delete(Object o){
        session.delete(o.getClass().getName(),o);
    }
    public Object fetchById(Class c,int id){
        return session.get(c.getName(),id);
    }
    public List fetchAll(Class c){
        Criteria criteria=session.createCriteria(c.getName());
        return criteria.list();
    }
}
