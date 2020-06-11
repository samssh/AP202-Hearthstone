package model.log;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import response.*;
import lombok.Getter;
import lombok.Setter;
import util.ResponseLogInfoVisitor;
import util.Visitable;

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

    public ResponseLog(Visitable<ResponseLogInfoVisitor> response, String username) {
        super(System.currentTimeMillis(), username);
        this.responseInfo = new ResponseLogInfo(response, time);
    }
}
