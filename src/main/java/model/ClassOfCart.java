package model;

import javax.persistence.*;

@Entity
public class ClassOfCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    @Column
    private String heroName;


    public ClassOfCart(){}

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }
}
