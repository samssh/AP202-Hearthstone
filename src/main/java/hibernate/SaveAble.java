package hibernate;


public interface SaveAble{
    public void delete();
    public void saveOrUpdate();
    public void load();
    public <E> E getId();
}
//in every class that using this interface most have two static method:
//public static SaveAble fetch();
//public static List<SaveAble> fetchAll();(optional)