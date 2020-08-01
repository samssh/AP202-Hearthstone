package ir.sam.hearthstone.model.log;

import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import lombok.Getter;
import lombok.Setter;
import ir.sam.hearthstone.response.Response;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@ToString(includeFieldNames = false)
public class ResponseLog extends Log {
    @OneToOne
    @Cascade(CascadeType.ALL)
    @Getter
    @Setter
    private ResponseLogInfo responseInfo;

    public ResponseLog() {
    }

    public ResponseLog(Response response, String username) {
        super(System.currentTimeMillis(), username);
        this.responseInfo = new ResponseLogInfo(response, time);
    }
}
