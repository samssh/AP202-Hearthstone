package ir.sam.hearthstone.model.main;

import ir.sam.hearthstone.hibernate.MapToStringConverter;
import ir.sam.hearthstone.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

@Entity
@EqualsAndHashCode(of = "name")
public abstract class Unit implements SaveAble, Cloneable, HasAction{
    @Id
    @Setter
    @Getter
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
    @Column
    @Convert(converter = MapToStringConverter.class)
    protected Map<ActionType,String> methods;

    public Unit() {
    }

    Unit(String name, String description) {
        this.name = name;
        this.description = description;
        methods = new HashMap<>();
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
