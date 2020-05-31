package model.account;

import hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
