package ir.sam.hearthstone.server.model.main;

import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "has_action")
public abstract class HasAction implements SaveAble, Cloneable {
    @Id
    @Getter
    @Setter
    protected String name;
    @Column
    @Getter
    @Setter
    protected String description;
    @Column(name = "class_name")
    @Getter
    @Setter
    protected String className;
    @Setter
    @Getter
    @Column
    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    @JoinTable(name = "has_action_methods", joinColumns = @JoinColumn(name = "has_action_name"))
    protected Map<ActionType, String> methods;

    public HasAction() {
    }

    @PostLoad
    private void postLoad() {
        this.methods = new HashMap<>(this.methods);
    }

    @Override
    public HasAction clone() {
        try {
            return (HasAction) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HasAction hasAction = (HasAction) o;
        return Objects.equals(name, hasAction.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
