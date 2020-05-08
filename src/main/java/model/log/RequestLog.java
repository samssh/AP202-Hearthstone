package model.log;

import lombok.Getter;
import lombok.Setter;
import server.Request;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class RequestLog extends Log {
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

    public RequestLog(Request request, String username) {
        super(System.nanoTime(), username);
        type = request.getClass().getSimpleName();
        if (request instanceof Request.LoginRequest){
            mode=((Request.LoginRequest) request).getMode();
            this.userName = ((Request.LoginRequest) request).getUserName();
            password = ((Request.LoginRequest) request).getPassword();
        }else if (request instanceof Request.SellCard){
            cardName = ((Request.SellCard) request).getCardName();
        }else if (request instanceof Request.BuyCard){
            cardName = ((Request.BuyCard) request).getCardName();
        }else if (request instanceof Request.CollectionDetails){
            name = ((Request.CollectionDetails) request).getName();
            classOfCard = ((Request.CollectionDetails) request).getClassOfCard();
            deckName = ((Request.CollectionDetails) request).getDeckName();
            mana = ((Request.CollectionDetails) request).getMana();
            lockMode = ((Request.CollectionDetails) request).getLockMode();
        }else if (request instanceof Request.NewDeck){
            deckName = ((Request.NewDeck) request).getDeckName();
            heroName = ((Request.NewDeck) request).getHeroName();
        }else if (request instanceof Request.DeleteDeck){
            deckName = ((Request.DeleteDeck) request).getDeckName();
        }else if (request instanceof Request.ChangeHeroDeck){
            deckName = ((Request.ChangeHeroDeck) request).getDeckName();
            heroName = ((Request.ChangeHeroDeck) request).getHeroName();
        }else if (request instanceof Request.ChangeDeckName){
            deckName = ((Request.ChangeDeckName) request).getOldDeckName();
            newDeckName = ((Request.ChangeDeckName) request).getNewDeckName();
        }else if (request instanceof Request.AddCardToDeck){
            deckName = ((Request.AddCardToDeck) request).getDeckName();
            cardName = ((Request.AddCardToDeck) request).getCardName();
        }else if (request instanceof Request.RemoveCardFromDeck){
            deckName = ((Request.RemoveCardFromDeck) request).getDeckName();
            cardName = ((Request.RemoveCardFromDeck) request).getCardName();
        }else if (request instanceof Request.SelectPassive){
            passiveName = ((Request.SelectPassive) request).getPassiveName();
        }else if (request instanceof Request.PlayCard){
            cardName = ((Request.PlayCard) request).getCardName();
        }
    }

    public RequestLog() {
    }
}
