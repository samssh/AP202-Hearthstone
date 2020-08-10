package ir.sam.hearthstone.server.model.log;

import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "header_log", schema = "log")
public class HeaderLog implements SaveAble {
    @Id
    @Setter
    @Getter
    private long id;
    @Column
    @Setter
    @Getter
    private String username;
    @Column
    @Setter
    @Getter
    private String password;
    @Column
    @Setter
    @Getter
    private String deletedAt;

    public HeaderLog() {
    }

    public HeaderLog(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "HeaderLog{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeaderLog headerLog = (HeaderLog) o;
        return id == headerLog.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}