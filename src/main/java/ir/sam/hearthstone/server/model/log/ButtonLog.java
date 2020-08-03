package ir.sam.hearthstone.server.model.log;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ButtonLog extends Log {
    @Column
    @Getter
    @Setter
    private String ButtonName;
    @Column
    @Getter
    @Setter
    private String menuName;

    public ButtonLog(String username, String buttonName, String menuName) {
        super(System.currentTimeMillis(), username);
        ButtonName = buttonName;
        this.menuName = menuName;
    }

    public ButtonLog() {
        super();
    }
}
