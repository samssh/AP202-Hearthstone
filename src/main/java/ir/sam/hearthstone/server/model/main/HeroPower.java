package ir.sam.hearthstone.server.model.main;

import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import java.util.HashMap;

@Entity
public class HeroPower extends HasAction implements SaveAble, Cloneable {
    @Column
    @Getter
    @Setter
    private int mana;


    public HeroPower() {
    }

    @PostLoad
    private void postLoad() {
        methods = new HashMap<>(methods);
    }

    @Override
    public HeroPower clone() {
        return (HeroPower) super.clone();
    }

    @Override
    public String toString() {
        return "HeroPower{" +
                "mana=" + mana +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", className='" + className + '\'' +
                ", methods=" + methods +
                '}';
    }
}
