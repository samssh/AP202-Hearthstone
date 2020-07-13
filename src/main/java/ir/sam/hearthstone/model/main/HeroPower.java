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
public class HeroPower implements SaveAble, Cloneable {
    @Id
    @Getter
    @Setter
    private String name;
    @Column
    @Getter
    @Setter
    private String description;
    @Column
    @Getter
    @Setter
    private int manaFrz;


    public HeroPower() {
    }

    public HeroPower(String name, String description, int manaFrz) {
        this.name = name;
        this.description = description;
        this.manaFrz = manaFrz;
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
