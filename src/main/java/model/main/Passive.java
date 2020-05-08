package model.main;

import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@ToString
public class Passive implements SaveAble {
    @Id
    @Getter
    @Setter
    String name;
    @Column
    @Getter
    @Setter
    String description;

    public Passive() {
    }

    public Passive(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
