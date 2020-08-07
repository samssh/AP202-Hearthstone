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
        return (Passive) super.clone();
    }
}
