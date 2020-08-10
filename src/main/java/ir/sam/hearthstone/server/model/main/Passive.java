package ir.sam.hearthstone.server.model.main;

import ir.sam.hearthstone.server.util.hibernate.SaveAble;

import javax.persistence.Entity;
import javax.persistence.PostLoad;
import java.util.HashMap;


@Entity
public class Passive extends HasAction implements SaveAble, Cloneable {
    public Passive() {
    }

    @PostLoad
    private void postLoad() {
        methods = new HashMap<>(methods);
    }

    @Override
    public Passive clone() {
        return (Passive) super.clone();
    }

    @Override
    public String toString() {
        return "Passive{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", className='" + className + '\'' +
                ", methods=" + methods +
                '}';
    }
}
