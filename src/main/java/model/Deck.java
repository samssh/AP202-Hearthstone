package model;


import hibernate.Connector;
import hibernate.ManualMapping;
import hibernate.SaveAble;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Deck implements SaveAble {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column
    @ManyToOne
    private Hero hero;
    @Transient
    private List<Cart> cartList;
    @ElementCollection
    private List<String> cartListId;

    {
        cartListId = new ArrayList<>();
        cartList = new ArrayList<>();
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }


    public void setId(Long id) {
        Id = id;
    }

    @Override
    public Long getId() {
        return Id;
    }

    @Override
    public void delete() {
        Connector connector = Connector.getConnector();
        connector.delete(this);
    }

    @Override
    public void saveOrUpdate() {
        Connector connector = Connector.getConnector();
        ManualMapping.saveOrUpdateList(cartListId, cartList);
        connector.saveOrUpdate(this);

    }

    @Override
    public void load() {
        setCartList(ManualMapping.fetchList(Cart.class, cartListId));
    }
}
