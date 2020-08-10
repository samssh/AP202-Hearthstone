package ir.sam.hearthstone.server.model.log;

import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "log")
public abstract class Log implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;
    @Column
    @Getter
    @Setter
    protected long time;
    @Column
    @Getter
    @Setter
    private String username;

    public Log(long time, String username) {
        this.time = time;
        this.username = username;
    }

    public Log() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return id == log.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
