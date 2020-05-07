package model.account;

import hibernate.Connector;
import hibernate.SaveAble;

import javax.persistence.*;

@Entity
public abstract class GameEvent implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public GameEvent() {
    }

    @Override
    public void delete(Connector connector) {
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate(Connector connector) {
        connector.saveOrUpdate(this);
    }

    @Override
    public void load(Connector connector) {
    }
}
