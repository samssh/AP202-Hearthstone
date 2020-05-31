package model.log;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import requests.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode(of = "id")
public class RequestLogInfo {
    @Id
    @Getter
    @Setter
    private long id;
    @Column
    @Getter
    @Setter
    private String type,userName, password, cardName, name;
    @Column
    @Getter
    @Setter
    private String classOfCard, deckName, passiveName, heroName, newDeckName;
    @Column
    @Getter
    @Setter
    private int mana, lockMode, mode;

    public RequestLogInfo() {
    }

    public RequestLogInfo(Request request,long id) {
        type = request.getClass().getSimpleName();
        this.id = id;
        if (request instanceof LoginRequest){
            mode=((LoginRequest) request).getMode();
            this.userName = ((LoginRequest) request).getUserName();
            password = ((LoginRequest) request).getPassword();
        }else if (request instanceof SellCard){
            cardName = ((SellCard) request).getCardName();
        }else if (request instanceof BuyCard){
            cardName = ((BuyCard) request).getCardName();
        }else if (request instanceof CollectionDetails){
            name = ((CollectionDetails) request).getName();
            classOfCard = ((CollectionDetails) request).getClassOfCard();
            deckName = ((CollectionDetails) request).getDeckName();
            mana = ((CollectionDetails) request).getMana();
            lockMode = ((CollectionDetails) request).getLockMode();
        }else if (request instanceof NewDeck){
            deckName = ((NewDeck) request).getDeckName();
            heroName = ((NewDeck) request).getHeroName();
        }else if (request instanceof DeleteDeck){
            deckName = ((DeleteDeck) request).getDeckName();
        }else if (request instanceof ChangeHeroDeck){
            deckName = ((ChangeHeroDeck) request).getDeckName();
            heroName = ((ChangeHeroDeck) request).getHeroName();
        }else if (request instanceof ChangeDeckName){
            deckName = ((ChangeDeckName) request).getOldDeckName();
            newDeckName = ((ChangeDeckName) request).getNewDeckName();
        }else if (request instanceof AddCardToDeck){
            deckName = ((AddCardToDeck) request).getDeckName();
            cardName = ((AddCardToDeck) request).getCardName();
        }else if (request instanceof RemoveCardFromDeck){
            deckName = ((RemoveCardFromDeck) request).getDeckName();
            cardName = ((RemoveCardFromDeck) request).getCardName();
        }else if (request instanceof SelectPassive){
            passiveName = ((SelectPassive) request).getPassiveName();
        }else if (request instanceof PlayCard){
            cardName = ((PlayCard) request).getCardName();
        }
    }
}
