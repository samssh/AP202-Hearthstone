package view;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private List<Menu> menuList;
    @Setter
    @Getter
    private String entry;
    @Setter
    @Getter
    private String key;
    @Setter
    @Getter
    private boolean hasEntryList;
    @Setter
    @Getter
    private List<String> entryList;



    {
        menuList = new ArrayList<>();
    }

    public Menu(String name, String key, boolean hasEntryList) {
        this.name = name;
        this.key = key;
        this.hasEntryList = hasEntryList;
    }
}
