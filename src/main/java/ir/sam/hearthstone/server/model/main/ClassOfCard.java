package ir.sam.hearthstone.server.model.main;

import ir.sam.hearthstone.server.util.hibernate.SaveAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "class_of_card")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClassOfCard implements SaveAble {
    @Id()
    @Column(name = "hero_name")
    @Setter
    @Getter
    @EqualsAndHashCode.Include
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
