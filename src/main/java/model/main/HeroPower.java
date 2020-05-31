package model.main;

import hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@ToString
@EqualsAndHashCode(of = "name")
public class HeroPower implements SaveAble {
    @Id
    @Getter
    @Setter
    private String name;
    @Column
    @Getter
    @Setter
    private String description;
    @Column
    @Getter
    @Setter
    private int manaFrz;


    public HeroPower() {
    }

    public HeroPower(String name, String description, int manaFrz) {
        this.name = name;
        this.description = description;
        this.manaFrz = manaFrz;
    }
}
