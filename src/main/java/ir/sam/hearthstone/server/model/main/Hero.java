package ir.sam.hearthstone.server.model.main;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Hero extends Unit {
    @Column
    @Setter
    @Getter
    private int hp;
    @OneToOne
    @JoinColumn(name = "hero_power_name")
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    @Setter
    @Getter
    private HeroPower heroPower;

    public Hero() {
    }

    @Override
    public String toString() {
        return "Hero{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", hp=" + hp +
                ", power=" + heroPower +
                '}';
    }

    @Override
    public Hero clone() {
        return (Hero) super.clone();
    }
}
