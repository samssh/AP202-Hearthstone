package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.view.graphics_engine.AnimationManger;
import ir.sam.hearthstone.view.graphics_engine.Speed;
import ir.sam.hearthstone.view.graphics_engine.effects.*;
import ir.sam.hearthstone.view.model.*;
import ir.sam.hearthstone.view.panel.PlayPanel;

import javax.swing.*;
import java.awt.*;

public class PlayEventExecutor {
    private final PlayPanel playPanel;

    public PlayEventExecutor(PlayPanel playPanel) {
        this.playPanel = playPanel;
    }

    public void execute(PlayDetails.Event event) {
        int side = event.getSide();
        int index = event.getIndex();
        switch (event.getType()) {
            case SET_HERO -> playPanel.getHero()[side].setUnitOverviewAnimated(event.getOverview());
            case SET_HERO_POWER -> playPanel.getHeroPower()[side].setUnitOverviewAnimated(event.getOverview());
            case PLAY_WEAPON -> AnimationManger.moveUnitToViewer(playPanel.getWeapon()[side], playPanel.getHand()[side],
                    event.getOverview(), event.getIndex(), playPanel.getAnimationManger());
            case PLAY_SPELL -> {
                Overview spell = playPanel.getHand()[side].removeModel(index, false);
                Point org = playPanel.getHand()[side].getPosition(index);
                org.translate(playPanel.getHand()[side].getX(), playPanel.getGround()[side].getY());
                Point dest = playPanel.getHero()[side ^ 1].getLocation();
                PaintByTime spellPainter = new Rotary(new OverviewPainter(spell));
                playPanel.getAnimationManger().addPainter(new LinearMotion(org, dest, spellPainter, x -> Math.pow(x, 2.1)));
            }
            case ADD_TO_GROUND -> playPanel.getGround()[side].addModel(index
                    , (MinionOverview) event.getOverview(), true);
            case ADD_TO_HAND -> playPanel.getHand()[side].addModel(0,(CardOverview) event.getOverview(), true);
            case CHANGE_IN_HAND -> playPanel.getHand()[side].changeModel(index, (CardOverview) event.getOverview());
            case REMOVE_FROM_HAND -> playPanel.getHand()[side].removeModel(index, true);
            case MOVE_FROM_HAND_TO_GROUND -> AnimationManger.moveAndChangeCard(playPanel.getGround()[side], playPanel.getHand()[side]
                    , (MinionOverview) event.getOverview(), event.getIndex(), event.getSecondIndex()
                    , playPanel.getAnimationManger());
            case MOVE_FROM_GROUND_TO_HAND -> AnimationManger.moveAndChangeCard(playPanel.getHand()[side], playPanel.getGround()[side]
                    , (CardOverview) event.getOverview(), event.getSecondIndex(), event.getIndex()
                    , playPanel.getAnimationManger());
            case CHANGE_IN_GROUND -> playPanel.getGround()[side].changeModel(index, (MinionOverview) event.getOverview());
            case REMOVE_FROM_GROUND -> playPanel.getGround()[side].removeModel(index, true);
            case ATTACK_MINION_TO_HERO -> attackMinionToHero(event);
            case ATTACK_MINION_TO_MINION -> attackMinionToMinion(event);
            case ATTACK_HERO_TO_MINION -> attackHeroToMinion(event);
            case ATTACK_HERO_TO_HERO -> attackHeroToHero(event);
            case ATTACK_HERO_POWER_TO_HERO -> attackHeroPowerToHero(event);
            case ATTACK_HERO_POWER_TO_MINION -> attackHeroPowerToMinion(event);
            case CHANGE_WEAPON -> playPanel.getWeapon()[side].setUnitOverviewAnimated(event.getOverview());
            case SHOW_MESSAGE -> SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(playPanel, event.getMessage()));
            case END_GAME -> {
                JOptionPane.showMessageDialog(playPanel, event.getMessage());
                playPanel.exit();
            }
        }
    }

    private PaintByTime getGoAndBack(Point org, Point dest, Overview overviewOld
            , Overview overviewNew, Speed speed) {
        PaintByTime old = new OverviewPainter(overviewOld);
        old = new LinearMotion(dest, org, old, speed);
        PaintByTime neW = new OverviewPainter(overviewNew);
        neW = new LinearMotion(dest, org, neW, speed);
        return new DoublePictureScale(neW, old, DoublePictureScale.NONE);
    }

    private void attackMinionToHero(PlayDetails.Event event) {
        int side = event.getSide();
        int index = event.getIndex();
        Point org = playPanel.getGround()[side].getPosition(index);
        org.translate(playPanel.getGround()[side].getX(), playPanel.getGround()[side].getY());
        Point dest = playPanel.getHero()[side ^ 1].getLocation();
        Overview old;
        Overview neW = event.getOverview();
        if (neW != null) {
            playPanel.getAnimationManger().addEndAnimationAction(() -> playPanel.getGround()[side]
                    .revertTemporaryRemove(index, (MinionOverview) neW));
            old = playPanel.getGround()[side].temporaryRemove(index);
        } else {
            old = playPanel.getGround()[side].removeModel(index);
        }
        playPanel.getAnimationManger().addPainter(getGoAndBack(org, dest, old, neW, x -> Math.pow(x, 1.7)));
    }

    private void attackMinionToMinion(PlayDetails.Event event) {
        int side = event.getSide();
        int index = event.getIndex();
        Point org = playPanel.getGround()[side].getPosition(index);
        org.translate(playPanel.getGround()[side].getX(), playPanel.getGround()[side].getY());
        Point dest = playPanel.getGround()[side ^ 1].getPosition(event.getSecondIndex());
        dest.translate(playPanel.getGround()[side ^ 1].getX(), playPanel.getGround()[side ^ 1].getY());
        Overview neW = event.getOverview();
        Overview old;
        if (neW != null) {
            old = playPanel.getGround()[side].temporaryRemove(index);
            playPanel.getAnimationManger().addEndAnimationAction(() ->
                    playPanel.getGround()[side].revertTemporaryRemove(index, (MinionOverview) neW));
        } else {
            old = playPanel.getGround()[side].removeModel(index);
        }
        playPanel.getAnimationManger().addPainter(getGoAndBack(org, dest, old, neW, x -> Math.pow(x, 1.7)));
    }

    private void attackHeroToMinion(PlayDetails.Event event) {
        int side = event.getSide();
        int index = event.getIndex();
        Point org = playPanel.getHero()[side].getLocation();
        Point dest = playPanel.getGround()[side ^ 1].getPosition(index);
        dest.translate(playPanel.getGround()[side ^ 1].getX(), playPanel.getGround()[side ^ 1].getY());
        Overview neW = event.getOverview1();
        Overview old = playPanel.getHero()[side].getUnitOverview();
        playPanel.getHero()[side].setUnitOverview(null);
        playPanel.getAnimationManger().addPainter(getGoAndBack(org, dest, old, neW, x -> Math.pow(x, 1.7)));
        playPanel.getWeapon()[side].setUnitOverviewAnimated(event.getOverview());
        playPanel.getAnimationManger().addEndAnimationAction(() ->
                playPanel.getHero()[side].setUnitOverview(event.getOverview1()));
    }

    private void attackHeroToHero(PlayDetails.Event event) {
        int side = event.getSide();
        Point org = playPanel.getHero()[side].getLocation();
        Point dest = playPanel.getHero()[side ^ 1].getLocation();
        HeroOverview attackerHero = (HeroOverview) playPanel.getHero()[side].getUnitOverview();
        playPanel.getHero()[side].setUnitOverview(null);
        playPanel.getAnimationManger().addPainter(getGoAndBack(org, dest, attackerHero
                , attackerHero, x -> Math.pow(x, 1.7)));
        playPanel.getWeapon()[side].setUnitOverviewAnimated(event.getOverview());
        playPanel.getAnimationManger().addEndAnimationAction(() ->
                playPanel.getHero()[side].setUnitOverview(attackerHero));

    }

    private void attackHeroPowerToHero(PlayDetails.Event event) {
        int side = event.getSide();
        Point org = playPanel.getHeroPower()[side].getLocation();
        Point dest = playPanel.getHero()[event.getSecondIndex()].getLocation();
        UnitOverview heroPower = playPanel.getHeroPower()[side].getUnitOverview();
        playPanel.getHeroPower()[side].setUnitOverview(null);
        playPanel.getAnimationManger().addPainter(getGoAndBack(org, dest, heroPower
                , heroPower, x -> Math.pow(x, 1.7)));
        playPanel.getAnimationManger().addEndAnimationAction(
                () -> playPanel.getHeroPower()[side].setUnitOverview(heroPower)
        );
    }

    private void attackHeroPowerToMinion(PlayDetails.Event event) {
        int side = event.getSide();
        Point org = playPanel.getHeroPower()[side].getLocation();
        Point dest = playPanel.getGround()[event.getSecondIndex()].getPosition(event.getIndex());
        dest.translate(playPanel.getGround()[event.getSecondIndex()].getX()
                , playPanel.getGround()[event.getSecondIndex()].getY());
        UnitOverview heroPower = playPanel.getHeroPower()[side].getUnitOverview();
        playPanel.getAnimationManger().addPainter(getGoAndBack(org, dest, heroPower
                , heroPower, x -> Math.pow(x, 1.7)));
        playPanel.getHeroPower()[side].setUnitOverview(null);
        playPanel.getAnimationManger().addEndAnimationAction(
                () -> playPanel.getHeroPower()[side].setUnitOverview(heroPower)
        );
    }
}