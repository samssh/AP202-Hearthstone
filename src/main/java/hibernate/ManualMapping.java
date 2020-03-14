package hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManualMapping {
    public static void saveOrUpdateList(List listId, List list) {
        listId.subList(0, listId.size()).clear();
        for (Object element : list) {
            ((SaveAble) element).saveOrUpdate();
            listId.add(((SaveAble) element).getId());
        }

    }

    public static void deleteList(List list){
        for (Object o : list) ((SaveAble)o).delete();
    }

    public static List fetchList(Class target, List listId) {
        List objectList = new ArrayList();
        for (Object s : listId) {
            objectList.add(fetch(target, (Serializable) s));
        }
        return objectList;
    }

    public static Object fetch(Class c, Serializable Id) {
        Connector connector = Connector.getConnector();
        Object o = connector.fetchById(c, Id);
        if (o == null)
            return null;
        ((SaveAble) o).load();
        return o;
    }

    public static List fetchAll(Class c) {
        Connector connector = Connector.getConnector();
        List list = connector.fetchAll(c);
        for (Object o : list) {
            ((SaveAble) o).load();
        }
        return list;
    }

}
