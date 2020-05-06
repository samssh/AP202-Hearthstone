package hibernate;


public interface SaveAble{
    void delete(Connector connector);
    void saveOrUpdate(Connector connector);
    void load(Connector connector);
}