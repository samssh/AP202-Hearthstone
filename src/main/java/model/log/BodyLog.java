package model.log;

import hibernate.Connector;
import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.logging.LogRecord;

@Entity
public class BodyLog implements SaveAble {
    @Id
    @Setter
    @Getter
    private String instant;
    @Column
    @Setter
    @Getter
    private String sourceClassName;
    @Column
    @Setter
    @Getter
    private String sourceMethodName;
    @Column
    @Setter
    @Getter
    private String message;
    @Column
    @Setter
    @Getter
    private long millis;
    @Column
    @Setter
    @Getter
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
