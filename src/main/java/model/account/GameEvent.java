package model.account;

import hibernate.SaveAble;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public abstract class GameEvent implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    public GameEvent() {
    }
}
