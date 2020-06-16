package ir.SAM.hearthstone.model.account;

import ir.SAM.hearthstone.hibernate.SaveAble;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(of = "id")
public abstract class GameEvent implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public GameEvent() {
    }
}
