package model;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private String name;
    private List<Menu> menuList;
    private String entry;
    private String key;
    private boolean hasEntryList;
    private List<String> entryList;

    {
        menuList = new ArrayList<>();
    }

    public Menu(String name, String key, boolean hasEntryList) {
        this.name = name;
        this.key = key;
        this.hasEntryList = hasEntryList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<String> entryList) {
        this.entryList = entryList;
    }

    public boolean isHasEntryList() {
        return hasEntryList;
    }

    public void setHasEntryList(boolean hasEntryList) {
        this.hasEntryList = hasEntryList;
    }
}
