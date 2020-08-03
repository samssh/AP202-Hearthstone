package ir.sam.hearthstone.server.model.main;

import ir.sam.hearthstone.server.util.hibernate.SaveAble;
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
public class HeroPower implements SaveAble, Cloneable, HasAction {
    @Id
    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private String name;
    @Column
    @Getter
    @Setter
    private String description;
    @Column
    @Getter
    @Setter
    private int manaFrz;
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


    public HeroPower() {
    }

    public HeroPower(String name, String description, int manaFrz) {
        this.name = name;
        this.description = description;
        this.manaFrz = manaFrz;
        this.methods = new HashMap<>();
    }

    @PostLoad
    private void postLoad() {
        methods = new HashMap<>(methods);
    }

    @Override
    public HeroPower clone() {
        try {
            return (HeroPower) super.clone();
        } catch (CloneNotSupportedException ignore) {
            // this shouldn't happen, since we are Cloneable
        }
        return null;
    }
}
