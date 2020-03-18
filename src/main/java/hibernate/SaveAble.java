package hibernate;


public interface SaveAble{
    void delete();
    void saveOrUpdate();
    void load();
    <E> E getId();
}
//in every class that using this interface most have two static method:
//public static SaveAble fetch();
//public static List<SaveAble> fetchAll();(optional)