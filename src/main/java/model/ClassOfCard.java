package model;

import hibernate.Connector;
import hibernate.SaveAble;

import javax.persistence.*;

@Entity
public class ClassOfCard implements SaveAble {
    @Id
    private String heroName;

    public ClassOfCard() {
    }

    public ClassOfCard(String heroName) {
        this.heroName = heroName;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public boolean isItForHero(Hero h){
        if (heroName.equals(h.getName())|| heroName.equals("Neutral")){
            return true;
        }
        return false;
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
