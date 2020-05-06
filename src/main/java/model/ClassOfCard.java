package model;

import hibernate.Connector;
import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
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
    public void delete(Connector connector) {
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate(Connector connector) {
        connector.saveOrUpdate(this);
    }

    @Override
    public void load(Connector connector) {}

    @Override
    public String toString() {
        return "{" +
                "heroName='" + heroName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassOfCard that = (ClassOfCard) o;
        return Objects.equals(heroName, that.heroName);
    }
}
