package ir.sam.hearthstone.server.model.main;

import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "class_of_card")
public class ClassOfCard implements SaveAble {
    @Id
    @Column(name = "hero_name")
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
        return "{" + "heroName='" + heroName + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassOfCard that = (ClassOfCard) o;
        return Objects.equals(heroName, that.heroName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heroName);
    }
}
