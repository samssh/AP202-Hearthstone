package ir.sam.hearthstone.model.main;

import ir.sam.hearthstone.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;


@Entity
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Passive implements SaveAble, Cloneable, HasAction {
    @Id
    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private String name;
    @Column
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    @Column
    protected String className;
    @Setter
    @Getter
    @Column
    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    protected Map<ActionType, String> methods;

    public Passive() {
    }

    public Passive(String name, String description) {
        this.name = name;
        this.description = description;
        this.methods = new HashMap<>();
    }

    @PostLoad
    private void postLoad() {
        methods = new HashMap<>(methods);
    }

    @Override
    public Passive clone() {
        try {
            return (Passive) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
        }
        return null;
    }
}
