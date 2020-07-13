package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.server.logic.game.behavioral_models.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static ir.sam.hearthstone.server.logic.game.Side.*;

public class GameState {
    @Getter
    @Setter
    private Side sideTurn;
    @Getter
    @Setter
    private int turnNumber;
    private final Map<Side, SideState> sideStateMap;

    GameState() {
        sideStateMap = new EnumMap<>(Side.class);
        sideStateMap.put(PLAYER_ONE,new SideState(PLAYER_ONE));
        sideStateMap.put(PLAYER_TWO,new SideState(PLAYER_TWO));
    }

    public int getMana(Side side) {
        return sideStateMap.get(side).mana;
    }

    public HeroLogic getHero(Side side) {
        return sideStateMap.get(side).hero;
    }

    public HeroPowerLogic getHeroPower(Side side) {
        return sideStateMap.get(side).heroPower;
    }

    public PassiveLogic getPassive(Side side) {
        return sideStateMap.get(side).passive;
    }

    public List<CardLogic> getHand(Side side) {
        return sideStateMap.get(side).hand;
    }

    public List<CardLogic> getDeck(Side side) {
        return sideStateMap.get(side).deck;
    }

    public List<MinionLogic> getGround(Side side) {
        return sideStateMap.get(side).ground;
    }

    public WeaponLogic getActiveWeapon(Side side) {
        return sideStateMap.get(side).weaponLogicOptional;
    }

    public void setMana(Side side,int mana) {
        sideStateMap.get(side).mana = mana;
    }

    public void setHero(Side side,HeroLogic hero) {
        sideStateMap.get(side).hero = hero;
    }

    void setHeroPower(Side side,HeroPowerLogic heroPower) {
        sideStateMap.get(side).heroPower = heroPower;
    }

    void setPassive(Side side,PassiveLogic passive) {
        sideStateMap.get(side).passive = passive;
    }

    public void setActiveWeapon(Side side,WeaponLogic activeWeapon) {
        sideStateMap.get(side).weaponLogicOptional = activeWeapon;
    }

    private static class SideState {
        @Getter
        private final Side side;
        @Getter
        private int mana;
        @Getter
        private HeroLogic hero;
        @Getter
        private HeroPowerLogic heroPower;
        @Getter
        private PassiveLogic passive;
        @Getter
        private final List<CardLogic> hand, deck;
        @Getter
        private final List<MinionLogic> ground;
        @Getter
        private WeaponLogic weaponLogicOptional;

        private SideState(Side side) {
            this.side = side;
            hand = new ArrayList<>();
            deck = new ArrayList<>();
            ground = new ArrayList<>();
        }
    }

}

