package model;

import hibernate.Connector;
import hibernate.SaveAble;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    public boolean isItForHero(Hero h) {
        return heroName.equals(h.getName()) || heroName.equals("Neutral");
    }

    @Override
    public void delete() {
        Connector connector = Connector.getConnector();
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate() {
        Connector connector = Connector.getConnector();
        connector.saveOrUpdate(this);
    }

    @Override
    public void load() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getId() {
        return heroName;
    }

    @Override
    public String toString() {
        return "{" +
                "heroName='" + heroName + '\'' +
                '}';
    }
}
