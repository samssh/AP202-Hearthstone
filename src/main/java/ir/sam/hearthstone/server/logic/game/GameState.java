package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.logic.game.behavioral_models.*;
import ir.sam.hearthstone.server.logic.game.events.GameEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static ir.sam.hearthstone.server.logic.game.Side.*;

public class GameState {
    @Getter
    @Setter
    private Side sideTurn;
    @Getter
    @Setter
    private int turnNumber;
    private final Map<Side, SideState> sideStateMap;
    @Getter
    private final List<PlayDetails.Event> events;
    @Getter
    private final List<GameEvent> gameEvents;

    GameState() {
        sideStateMap = new EnumMap<>(Side.class);
        sideStateMap.put(PLAYER_ONE, new SideState(PLAYER_ONE));
        sideStateMap.put(PLAYER_TWO, new SideState(PLAYER_TWO));
        events = new LinkedList<>();
        gameEvents = new LinkedList<>();
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
        return sideStateMap.get(side).weaponLogic;
    }

    public void setMana(Side side, int mana) {
        sideStateMap.get(side).mana = mana;
    }

    public void setHero(Side side, HeroLogic hero) {
        sideStateMap.get(side).hero = hero;
    }

    void setHeroPower(Side side, HeroPowerLogic heroPower) {
        sideStateMap.get(side).heroPower = heroPower;
    }

    void setPassive(Side side, PassiveLogic passive) {
        sideStateMap.get(side).passive = passive;
    }

    public Stream<ComplexLogic> getSideStream(Side side) {
        return sideStateMap.get(side).getStream();
    }

    public int[] getMana(){
        int[] mana = new int[2];
        mana[0]= getMana(PLAYER_ONE);
        mana[1]= getMana(PLAYER_TWO);
        return mana;
    }


    public void setActiveWeapon(Side side, WeaponLogic activeWeapon) {
        sideStateMap.get(side).weaponLogic = activeWeapon;
    }

    private static class SideState {
        @Getter
        private final Side side;
        @Getter
        private int mana;
        @Getter
        private HeroLogic hero;//0
        @Getter
        private HeroPowerLogic heroPower;//1
        @Getter
        private PassiveLogic passive;//2
        @Getter
        private final List<CardLogic> hand, deck;
        @Getter
        private final List<MinionLogic> ground;//4 + index
        @Getter
        private WeaponLogic weaponLogic;// 3

        private SideState(Side side) {
            this.side = side;
            hand = new ArrayList<>();
            deck = new ArrayList<>();
            ground = new ArrayList<>();
        }

        Stream<ComplexLogic> getStream() {
            return StreamSupport.stream(Spliterators.spliterator(new Iterator()
                    , 3 + ground.size() + (weaponLogic == null ? 0 : 1),
                    Spliterator.NONNULL & Spliterator.DISTINCT), false);
        }

        private class Iterator implements java.util.Iterator<ComplexLogic> {
            private int mode = 0;

            @Override
            public boolean hasNext() {
                if (mode < 3)
                    return true;
                if (mode == 3)
                    return weaponLogic != null || ground.size() != 0;
                return mode - 3 <= ground.size();
            }

            @Override
            public ComplexLogic next() {
                mode++;
                switch (mode) {
                    case 1:
                        return hero;
                    case 2:
                        return heroPower;
                    case 3:
                        return passive;
                    case 4:
                        if (weaponLogic != null) return weaponLogic;
                        else {
                            mode++;
                            return ground.get(0);
                        }
                    default:
                        return ground.get(mode - 5);
                }
            }
        }
    }


}

