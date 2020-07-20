package ir.sam.hearthstone.view.util;

import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.view.graphics_engine.AnimationManger;
import ir.sam.hearthstone.view.graphics_engine.Speed;
import ir.sam.hearthstone.view.graphics_engine.effects.*;
import ir.sam.hearthstone.view.model.CardOverview;
import ir.sam.hearthstone.view.model.HeroOverview;
import ir.sam.hearthstone.view.model.MinionOverview;
import ir.sam.hearthstone.view.model.Overview;
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
            case SET_HERO:
                playPanel.getHero()[side].setUnitOverviewAnimated(event.getOverview());
                break;
            case SET_HERO_POWER:
                playPanel.getHeroPower()[side].setUnitOverviewAnimated(event.getOverview());
                break;
            case PLAY_WEAPON:
                AnimationManger.moveUnitToViewer(playPanel.getWeapon()[side], playPanel.getHand()[side],
                        event.getOverview(), event.getIndex(), playPanel.getAnimationManger());
                break;
            case PLAY_SPELL:
                Overview spell = playPanel.getHand()[side].removeModel(index, false);
                Point org = playPanel.getHand()[side].getPosition(index);
                org.translate(playPanel.getHand()[side].getX(), playPanel.getGround()[side].getY());
                Point dest = playPanel.getHero()[side ^ 1].getLocation();
                PaintByTime spellPainter = new Rotary(new OverviewPainter(spell));
                playPanel.getAnimationManger().addPainter(new LinearMotion(org, dest, spellPainter, x -> Math.pow(x, 2.1)));
                break;
            case ADD_TO_GROUND:
                playPanel.getGround()[side].addModel(index
                        , (MinionOverview) event.getOverview(), true);
                break;
            case ADD_TO_HAND:
                playPanel.getHand()[side].addModel((CardOverview) event.getOverview(), true);
                break;
            case CHANGE_IN_HAND:
                playPanel.getHand()[side].changeModel(index, (CardOverview) event.getOverview());
                break;
            case REMOVE_FROM_HAND:
                playPanel.getHand()[side].removeModel(index, true);
                break;
            case MOVE_FROM_HAND_TO_GROUND:
                AnimationManger.moveAndChangeCard(playPanel.getGround()[side], playPanel.getHand()[side]
                        , (MinionOverview) event.getOverview(), event.getIndex(), event.getSecondIndex()
                        , playPanel.getAnimationManger());
                break;
            case MOVE_FROM_GROUND_TO_HAND:
                AnimationManger.moveAndChangeCard(playPanel.getHand()[side], playPanel.getGround()[side]
                        , (CardOverview) event.getOverview(), event.getSecondIndex(), event.getIndex()
                        , playPanel.getAnimationManger());
                break;
            case CHANGE_IN_GROUND:
                playPanel.getGround()[side].changeModel(index, (MinionOverview) event.getOverview());
                break;
            case REMOVE_FROM_GROUND:
                playPanel.getGround()[side].removeModel(index, true);
                break;
            case ATTACK_MINION_TO_HERO:
                attackMinionToHero(event);
                break;
            case ATTACK_MINION_TO_MINION:
                attackMinionToMinion(event);
                break;
            case ATTACK_HERO_TO_MINION:
                attackHeroToMinion(event);
                break;
            case ATTACK_HERO_TO_HERO:
                attackHeroToHero(event);
                break;
            case SHOW_MESSAGE:
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(playPanel, event.getMessage()));
                break;
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
        Overview old = playPanel.getGround()[side].removeModel(index);
        Overview neW = event.getOverview();
        playPanel.getAnimationManger().addPainter(getGoAndBack(org, dest, old, neW, x -> Math.pow(x, 1.7)));
        if (neW != null)
            playPanel.getAnimationManger().addEndAnimationAction(() -> playPanel.getGround()[side]
                    .addModel(index, (MinionOverview) neW));
    }

    private void attackMinionToMinion(PlayDetails.Event event) {
        int side = event.getSide();
        int index = event.getIndex();
        Point org = playPanel.getGround()[side].getPosition(index);
        org.translate(playPanel.getGround()[side].getX(), playPanel.getGround()[side].getY());
        Point dest = playPanel.getGround()[side ^ 1].getPosition(event.getSecondIndex());
        dest.translate(playPanel.getGround()[side ^ 1].getX(), playPanel.getGround()[side ^ 1].getY());
        Overview neW = event.getOverview();
        Overview old = playPanel.getGround()[side].removeModel(index);
        playPanel.getAnimationManger().addPainter(getGoAndBack(org, dest, old, neW, x -> Math.pow(x, 1.7)));
        if (neW != null)
            playPanel.getGround()[side].addModel(index, (MinionOverview) neW);
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
}