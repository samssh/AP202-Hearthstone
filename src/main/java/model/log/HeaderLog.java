package model.log;

import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class HeaderLog implements SaveAble {
    @Id
    @Setter
    @Getter
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