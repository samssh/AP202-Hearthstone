package hibernate;

public interface SaveAble {
    public Integer save();
    public void update();
    public void delete();
    public void saveOrUpdate();
    public void load();
}
