package ir.sam.hearthstone.model.main;

import ir.sam.hearthstone.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Unit implements SaveAble, Cloneable, HasAction {
    @Id
    @Setter
    @Getter
    @EqualsAndHashCode.Include
    protected String name;
    @Setter
    @Getter
    @Column
    protected String description;
    @Getter
    @Setter
    @Column
    protected String className;
    @Setter
    @Getter
    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    protected Map<ActionType, String> methods;

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
