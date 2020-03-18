package model;

import hibernate.Connector;
import hibernate.SaveAble;

import javax.persistence.*;

@Entity
public class HeaderLog implements SaveAble {
    @Id
    private Long id;
    @Column
    private String userName;
    @Column
    private String password;
    @Column
    private String deletedAt;

    public HeaderLog() {
    }

    public HeaderLog(Long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public void delete() {
        Connector.getConnector().delete(this);
    }

    @Override
    public void saveOrUpdate() {
        Connector.getConnector().saveOrUpdate(this);
    }

    @Override
    public void load() {
    }

    @Override
    public Long getId() {
        return id;
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