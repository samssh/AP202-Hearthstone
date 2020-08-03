package ir.sam.hearthstone.server.controller.logic.game;

import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.*;
import ir.sam.hearthstone.server.controller.logic.game.events.GameEvent;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Stream;

import static ir.sam.hearthstone.server.controller.logic.game.Side.PLAYER_ONE;
import static ir.sam.hearthstone.server.controller.logic.game.Side.PLAYER_TWO;

public class GameState {
    @Getter
    @Setter
    protected Side sideTurn;
    @Getter
    @Setter
    protected int turnNumber;
    protected final Map<Side, SideState> sideStateMap;
    @Getter
    protected final List<PlayDetails.Event> events;
    @Getter
    protected final List<GameEvent> gameEvents;

    protected GameState() {
        sideStateMap = new EnumMap<>(Side.class);
        sideStateMap.put(PLAYER_ONE, new SideState());
        sideStateMap.put(PLAYER_TWO, new SideState());
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
        return sideStateMap.get(side).weapon;
    }

    public QuestLogic getActiveQuest(Side side) {
        return sideStateMap.get(side).quest;
    }

    public void setMana(Side side, int mana) {
        sideStateMap.get(side).mana = mana;
    }

    public void setHero(Side side, HeroLogic hero) {
        sideStateMap.get(side).hero = hero;
    }

    public void setHeroPower(Side side, HeroPowerLogic heroPower) {
        sideStateMap.get(side).heroPower = heroPower;
    }

    public void setPassive(Side side, PassiveLogic passive) {
        sideStateMap.get(side).passive = passive;
    }

    public Stream<ComplexLogic> getSideStream(Side side) {
        return sideStateMap.get(side).getStream();
    }

    public int[] getMana() {
        int[] mana = new int[2];
        mana[0] = getMana(PLAYER_ONE);
        mana[1] = getMana(PLAYER_TWO);
        return mana;
    }

    public void setActiveWeapon(Side side, WeaponLogic activeWeapon) {
        sideStateMap.get(side).weapon = activeWeapon;
    }

    public void setActiveQuest(Side side, QuestLogic quest) {
        sideStateMap.get(side).quest = quest;
    }

    public void changeTaunts(Side side, int diff) {
        sideStateMap.get(side).taunts += diff;
    }

    public int getTaunts(Side side) {
        return sideStateMap.get(side).taunts;
    }

    public MinionLogic getSelectedMinionOnHand(Side side) {
        return sideStateMap.get(side).selectedMinionOnHand;
    }

    public void setSelectedMinionOnHand(Side side, MinionLogic minionLogic) {
        sideStateMap.get(side).selectedMinionOnHand = minionLogic;
    }

    public MinionLogic getSelectedMinionOnGround(Side side) {
        return sideStateMap.get(side).selectedMinionOnGround;
    }

    public void setSelectedMinionOnGround(Side side, MinionLogic minionLogic) {
        sideStateMap.get(side).selectedMinionOnGround = minionLogic;
    }

    public ComplexLogic getWaitForTarget(Side side) {
        return sideStateMap.get(side).waitForTarget;
    }

    public void setWaitForTarget(Side side, ComplexLogic waitForTarget) {
        sideStateMap.get(side).waitForTarget = waitForTarget;
    }

    public boolean isHeroSelected(Side side) {
        return sideStateMap.get(side).heroSelected;
    }

    public void setHeroSelected(Side side, boolean heroSelected) {
        sideStateMap.get(side).heroSelected = heroSelected;
    }

    public boolean isHeroPowerSelected(Side side) {
        return sideStateMap.get(side).heroPowerSelected;
    }

    public void setHeroPowerSelected(Side side, boolean heroPowerSelected) {
        sideStateMap.get(side).heroPowerSelected = heroPowerSelected;
    }

    public List<ComplexLogic> getActiveUnits(Side side) {
        return sideStateMap.get(side).activeUnits;
    }

    public void resetSelected(Side side) {
        sideStateMap.get(side).resetSelected();
    }

    protected static class SideState {
        protected int mana;
        protected HeroLogic hero;
        protected HeroPowerLogic heroPower;
        protected PassiveLogic passive;
        protected final List<CardLogic> hand, deck;
        protected final List<MinionLogic> ground;
        protected final List<ComplexLogic> activeUnits;
        protected WeaponLogic weapon;
        protected QuestLogic quest;
        protected MinionLogic selectedMinionOnGround, selectedMinionOnHand; // in hand or ground
        protected ComplexLogic waitForTarget; // some thing special
        protected boolean heroSelected, heroPowerSelected;
        protected int taunts;


        protected SideState() {
            hand = new Stack<>();
            deck = new Stack<>();
            ground = new Stack<>();
            activeUnits = new Stack<>();
        }

        protected void resetSelected() {
            selectedMinionOnGround = null;
            selectedMinionOnHand = null;
            heroSelected = false;
            heroPowerSelected = false;
        }

        protected Stream<ComplexLogic> getStream() {
            /*Array*/
            List<ComplexLogic> complexLogicList = new Stack<>(/*hand.size() + 5*/);
            complexLogicList.add(hero);
            complexLogicList.add(heroPower);
            complexLogicList.add(passive);
            if (weapon != null)
                complexLogicList.add(weapon);
            if (quest != null)
                complexLogicList.add(quest);
            complexLogicList.addAll(ground);
            complexLogicList.addAll(activeUnits);
            return complexLogicList.stream();
        }
    }
}