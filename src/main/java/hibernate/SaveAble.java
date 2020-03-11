package hibernate;

import java.io.Serializable;

public interface SaveAble {
    //in every class that using this interface most have two static method
    //public static SaveAble fetch();
    //public static List<SaveAble> fetchAll();
    public void update();
    public void delete();
    public void saveOrUpdate();
    public void load();
    public Serializable getId();
}
