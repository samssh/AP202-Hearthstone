package model.log;

import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public abstract class Log implements SaveAble {
    @Id
    @Getter
    @Setter
    private long time;
    @Getter
    @Setter
    private String username;

    public Log(long time, String username) {
        this.time = time;
        this.username = username;
    }

    public Log() {
    }
}
