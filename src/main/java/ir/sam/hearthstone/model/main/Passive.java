package ir.sam.hearthstone.model.main;

import ir.sam.hearthstone.hibernate.MapToStringConverter;
import ir.sam.hearthstone.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;


@Entity
@ToString
@EqualsAndHashCode(of = "name")
public class Passive implements SaveAble, Cloneable, HasAction {
    @Id
    @Getter
    @Setter
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
    @Convert(converter = MapToStringConverter.class)
    protected Map<ActionType, String> methods;

    public Passive() {
    }

    public Passive(String name, String description) {
        this.name = name;
        this.description = description;
        this.methods = new HashMap<>();
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
