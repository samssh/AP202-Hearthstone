package model.log;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import response.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ResponseLog extends Log {
    @OneToOne
    @Cascade(CascadeType.ALL)
    @Getter
    @Setter
    private ResponseLogInfo responseInfo;

    public ResponseLog() {
    }

    public ResponseLog(Response response, String username) {
        super(System.nanoTime(), username);
        this.responseInfo = new ResponseLogInfo(response, time);
    }
}
