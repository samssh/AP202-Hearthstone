package ir.sam.hearthstone.server.logic.game;

import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.logic.game.behavioral_models.*;
import ir.sam.hearthstone.server.logic.game.events.GameEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Stream;

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

    void setHeroPower(Side side, HeroPowerLogic heroPower) {
        sideStateMap.get(side).heroPower = heroPower;
    }

    void setPassive(Side side, PassiveLogic passive) {
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

    public void changeTaunts(Side side,int diff){
        sideStateMap.get(side).taunts+=diff;
    }

    public int getTaunts(Side side){
        return sideStateMap.get(side).taunts;
    }

    public MinionLogic getSelectedMinion(Side side){
        return sideStateMap.get(side).selectedMinion;
    }

    public boolean isHeroSelected(Side side){
        return sideStateMap.get(side).heroSelected;
    }

    public void setSelectedMinion(Side side,MinionLogic minionLogic){
        sideStateMap.get(side).selectedMinion = minionLogic;
    }

    public void setHeroSelected(Side side, boolean heroSelected){
        sideStateMap.get(side).heroSelected = heroSelected;
    }

    public List<ComplexLogic> getActiveUnits(Side side){
        return sideStateMap.get(side).activeUnits;
    }

    private static class SideState {
        private int mana;
        private HeroLogic hero;
        private HeroPowerLogic heroPower;
        private PassiveLogic passive;
        private final List<CardLogic> hand, deck;
        private final List<MinionLogic> ground;
        private final List<ComplexLogic> activeUnits;
        private WeaponLogic weapon;
        private QuestLogic quest;
        private MinionLogic selectedMinion;
        private boolean heroSelected;
        private int taunts;


        private SideState() {
            hand = new ArrayList<>();
            deck = new ArrayList<>();
            ground = new ArrayList<>();
            activeUnits = new ArrayList<>();
        }

        private Stream<ComplexLogic> getStream() {
            ArrayList<ComplexLogic> complexLogicList = new ArrayList<>(hand.size() + 5);
            complexLogicList.add(hero);
            complexLogicList.add(heroPower);
            complexLogicList.add(passive);
            if (weapon != null)
                complexLogicList.add(weapon);
            if (quest != null)
                complexLogicList.add(quest);
            complexLogicList.addAll(hand);
            complexLogicList.addAll(activeUnits);
            return complexLogicList.stream();
        }
    }
}

