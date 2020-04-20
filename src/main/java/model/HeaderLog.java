package model;

import hibernate.Connector;
import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class HeaderLog implements SaveAble {
    @Id
    @Setter
    @Getter
    private Long id;
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

    @Override
    public String toString() {
        return "HeaderLog{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                '}';
    }
}