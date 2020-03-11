package hibernate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public static List fetchList(Class c, List listId, Class Id) {
        List objectList = new ArrayList();
        try {
            Method fetch = c.getDeclaredMethod("fetch",Id);
            for (Object s : listId) {
                objectList.add(fetch.invoke(null, Id.cast(s)));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return objectList;
    }

}
