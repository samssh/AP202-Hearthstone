package hibernate;


public interface SaveAble{
    void delete();
    void saveOrUpdate();
    void load();
    <E> E getId();
}