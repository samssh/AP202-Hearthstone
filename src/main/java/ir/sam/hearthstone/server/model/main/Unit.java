package ir.sam.hearthstone.server.model.main;


import javax.persistence.Entity;
import javax.persistence.PostLoad;
import java.util.HashMap;

@Entity
public abstract class Unit extends HasAction {

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
        return (Unit) super.clone();
    }
}
