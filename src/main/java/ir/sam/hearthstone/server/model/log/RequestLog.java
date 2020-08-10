package ir.sam.hearthstone.server.model.log;

import ir.sam.hearthstone.server.model.requests.Request;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class RequestLog extends Log {
    @OneToOne
    @Cascade(CascadeType.ALL)
    @Getter
    @Setter
    private RequestLogInfo requestInfo;

    public RequestLog() {
    }

    public RequestLog(Request request, String username) {
        super(System.currentTimeMillis(), username);
        this.requestInfo = new RequestLogInfo(request);
    }
}
