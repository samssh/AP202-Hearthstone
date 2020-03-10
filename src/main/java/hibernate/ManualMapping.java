package hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ManualMapping {
    public static void saveOrUpdateList(List listId, List list, Object o, String getIdName) {
        Class c = o.getClass();
        try {
            Method getId = c.getDeclaredMethod(getIdName);
            if (listId.size() > 0)
                listId.subList(0, listId.size()).clear();
            for (Object element : list) {
                ((SaveAble) element).saveOrUpdate();
                listId.add(getId.invoke(o));
            }
        }catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }
    }

    public static List fetchList(Class c, List listId, String fetchName) {
        List objectList = new ArrayList();
        try {
            Method fetch = c.getDeclaredMethod(fetchName, Serializable.class);
            for (Object s : listId) {
                objectList.add(c.cast(fetch.invoke(null, (Serializable) s)));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return objectList;
    }

}
