package ir.sam.hearthstone.server.model.log;

import ir.sam.hearthstone.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HeaderLog implements SaveAble {
    @Id
    @Setter
    @Getter
    @EqualsAndHashCode.Include
    private long id;
    @Column
    @Setter
    @Getter
    private String userName;
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

    public HeaderLog(Long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "HeaderLog{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                '}';
    }
}