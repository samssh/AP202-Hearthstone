package ir.sam.hearthstone.model.main;

import ir.sam.hearthstone.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@ToString
@EqualsAndHashCode(of = "name")
public class Passive implements SaveAble, Cloneable {
    @Id
    @Getter
    @Setter
    private String name;
    @Column
    @Getter
    @Setter
    private String description;

    public Passive() {
    }

    public Passive(String name, String description) {
        this.name = name;
        this.description = description;
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
