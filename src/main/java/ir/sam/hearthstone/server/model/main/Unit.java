package ir.sam.hearthstone.server.model.main;

import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public abstract class Unit extends HasAction implements SaveAble, Cloneable {

    public Unit() {
    }

    Unit(String name, String description) {
        this.name = name;
        this.description = description;
        methods = new HashMap<>();
    }

    @PostLoad
    private void postLoad() {
        methods = new HashMap<>(methods);
    }

    @Override
    public Unit clone() {
        try {
            return (Unit) super.clone();
        } catch (CloneNotSupportedException ignore) {
            // this shouldn't happen, since we are Cloneable
        }
        return null;
    }
}
