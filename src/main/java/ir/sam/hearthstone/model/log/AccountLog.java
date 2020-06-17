package ir.sam.hearthstone.model.log;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AccountLog extends Log{
    @Column
    @Setter
    @Getter
    private String type;

    public AccountLog(String username, String type) {
        super(System.currentTimeMillis(), username);
        this.type = type;
    }

    public AccountLog() {
    }
}
