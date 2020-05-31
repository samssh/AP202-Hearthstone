package model.main;

import hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@EqualsAndHashCode(of = "heroName")
public class ClassOfCard implements SaveAble {
    @Id
    @Setter
    @Getter
    private String heroName;

    public ClassOfCard() {
    }

    public ClassOfCard(String heroName) {
        this.heroName = heroName;
    }

    @Override
    public String toString() {
        return "{" +
                "heroName='" + heroName + '\'' +
                '}';
    }
}
