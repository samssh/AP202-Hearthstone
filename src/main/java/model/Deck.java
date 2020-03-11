package model;


import javax.persistence.*;
import java.util.List;
@Entity
public class Deck {
    @Id
    private Long Id;
    @Column
    @ManyToOne
    private Hero hero;
    @Transient
    private List<Cart> cartList;
    @ElementCollection
    private List<String> cartListId;

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

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
