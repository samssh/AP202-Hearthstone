package ir.sam.hearthstone.server.model.main;

import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;

@Entity
@ToString
public class HeroPower extends HasAction implements SaveAble, Cloneable {
    @Column
    @Getter
    @Setter
    private int manaFrz;


    public HeroPower() {
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
