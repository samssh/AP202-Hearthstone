package ir.sam.hearthstone.server.logic.game.behavioral_models;

import ir.sam.hearthstone.model.main.ActionType;
import ir.sam.hearthstone.model.main.Hero;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.GameState;
import ir.sam.hearthstone.server.logic.game.Side;
import ir.sam.hearthstone.view.model.HeroOverview;
import lombok.Getter;
import lombok.Setter;

public class HeroLogic extends CharacterLogic implements AttackAble {
    @Getter
    @Setter
    private Hero hero;
    @Getter
    @Setter
    protected int hp, defence;


    public HeroLogic(Side side, Hero hero) {
        super(side);
        this.hero = hero.clone();
        hp = hero.getHpFrz();
        defence=0;
    }

    public void setDefence(int defence,GameState gameState){
        if (defence!=this.defence){
            setDefence(defence);
            addChangeEvent(gameState);
        }
    }

    private void dealDamage(int damage, AbstractGame game, boolean sendEvent) {
        defence -= damage;
        if (defence < 0) {
            hp += defence;
            defence = 0;
        }
        if (sendEvent)addChangeEvent(game.getGameState());
        if (hp<0){
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.END_GAME)
                    .setMessage(side+" lose").build();
            game.getGameState().getEvents().add(event);
        }
    }

    /**
     * attack enemy to this minion
     * this can be hero with weapon or minion
     *
     * @param damage damage that deals to this minion
     */
    public void dealMinionDamage(int damage, AbstractGame game, boolean sendEvent) {
        dealDamage(damage,game,sendEvent);
    }

    private void addChangeEvent(GameState gameState) {
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.SET_HERO)
                .setOverview(getHeroOverview()).setSide(side.getIndex()).build();
        gameState.getEvents().add(event);
    }

    public HeroOverview getHeroOverview() {
        return new HeroOverview(this);
    }

    @Override
    public String getName() {
        return hero.getName();
    }

    @Override
    public void dealMinionDamage(int damage, AbstractGame game) {

    }
}
