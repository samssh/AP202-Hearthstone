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
public class Passive implements SaveAble {
    @Id
    @Getter
    @Setter
    private String name;
    @Column
    @Getter
    @Setter
    private String description;

    public Passive() {
    }

    public Passive(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
