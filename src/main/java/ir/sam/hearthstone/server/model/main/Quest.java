package ir.sam.hearthstone.server.model.main;

import javax.persistence.Entity;

@Entity
public class Quest extends Spell {
    public Quest() {
    }

    @Override
    public Quest clone() {
        return (Quest) super.clone();
    }
}
