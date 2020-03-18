package model;

import hibernate.Connector;
import hibernate.SaveAble;

import javax.persistence.*;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

@Entity
public class BodyLog implements SaveAble {
    @Id
    private String instant;
    @Column
    private String sourceClassName;
    @Column
    private String sourceMethodName;
    @Column
    private String message;
    @Column
    private long millis;
    @Column
    private long headId;

    public BodyLog() {
    }

    public BodyLog(LogRecord logRecord) {
        Date date = new Date(logRecord.getMillis());
        this.instant = date.toInstant().toString();
        this.sourceClassName = logRecord.getSourceClassName();
        this.sourceMethodName = logRecord.getSourceMethodName();
        this.message = logRecord.getMessage();
        this.millis = logRecord.getMillis();
    }

    public String getInstant() {
        return instant;
    }

    public void setInstant(String instant) {
        this.instant = instant;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getSourceMethodName() {
        return sourceMethodName;
    }

    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public long getHeadId() {
        return headId;
    }

    public void setHeadId(long headId) {
        this.headId = headId;
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
    public String getId() {
        return instant;
    }

    @Override
    public String toString() {
        return "BodyLog{" +
                "instant='" + instant + '\'' +
                ", sourceClassName='" + sourceClassName + '\'' +
                ", sourceMethodName='" + sourceMethodName + '\'' +
                ", message='" + message + '\'' +
                ", millis=" + millis +
                ", headId=" + headId +
                '}';
    }
}
