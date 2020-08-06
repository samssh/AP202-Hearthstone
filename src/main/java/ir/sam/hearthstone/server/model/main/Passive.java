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
public class Passive extends HasAction implements SaveAble, Cloneable {
    public Passive() {
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
