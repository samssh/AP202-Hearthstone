package ir.sam.hearthstone.server.controller.logic.game;

import ir.sam.hearthstone.server.controller.Constants;
import ir.sam.hearthstone.server.controller.logic.game.api.Game;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.*;
import ir.sam.hearthstone.server.controller.logic.game.events.*;
import ir.sam.hearthstone.server.controller.logic.game.visitors.ActionHolder;
import ir.sam.hearthstone.server.controller.logic.game.visitors.ActionHolderBuilder;
import ir.sam.hearthstone.server.model.client.CardOverview;
import ir.sam.hearthstone.server.model.main.ActionType;
import ir.sam.hearthstone.server.model.main.Minion;
import ir.sam.hearthstone.server.model.response.PlayDetails;
import ir.sam.hearthstone.server.model.response.Response;
import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.TaskTimer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ir.sam.hearthstone.server.model.response.PlayDetails.EventType.ADD_TO_HAND;
import static ir.sam.hearthstone.server.model.response.PlayDetails.EventType.SHOW_MESSAGE;

public abstract class AbstractGame implements Game {
    public static void visitAll(AbstractGame game, ActionType actionType, CharacterLogic characterLogic, Side side) {
        game.getGameState().getSideStream(side).forEach(complexLogic ->
                game.getActionHolderMap().get(actionType)
                        .doAction(complexLogic, characterLogic, game));
    }

    @Getter
    protected final GameState gameState;
    @Getter
    protected final ModelLoader modelLoader;
    @Getter
    protected final Map<ActionType, ActionHolder> actionHolderMap;
    @Getter
    protected long turnStartTime;
    @Getter
    protected final TaskTimer timer;
    @Getter
    protected boolean running;

    public AbstractGame(GameState gameState, ModelLoader modelLoader) {
        this.gameState = gameState;
        this.modelLoader = modelLoader;
        actionHolderMap = ActionHolderBuilder.getAllActionHolders(modelLoader);
        timer = new TaskTimer();
        running = true;
    }

    @Override
    public abstract void selectHero(Side client, Side side);

    protected void selectHero(Side side) {
        if (gameState.getWaitForTarget(gameState.getSideTurn()) != null) {
            actionHolderMap.get(ActionType.Do_ACTION_FOR_TARGET)
                    .doAction(gameState.getWaitForTarget(gameState.getSideTurn())
                            , gameState.getHero(side), this);
            return;
        }
        HeroLogic selected = gameState.getHero(side);
        if (gameState.isHeroPowerSelected(gameState.getSideTurn())) {
            getActionHolderMap().get(ActionType.DO_ACTION).doAction(
                    gameState.getHeroPower(gameState.getSideTurn()), selected, this);
            return;
        }
        if (side != gameState.getSideTurn()) {
            if (gameState.getSelectedMinionOnGround(side.getOther()) != null) {
                MinionLogic attacker = gameState.getSelectedMinionOnGround(side.getOther());
                if (gameState.getSelectedMinionOnGround(side.getOther()).canAttackToHero(gameState)) {
                    attackMinionToHero(attacker, side);
                    gameState.resetSelected(side.getOther());
                }
            } else if (gameState.isHeroSelected(side.getOther())) {
                attackHeroToHero(side.getOther());
                gameState.resetSelected(side.getOther());
            }
        } else if (gameState.getActiveWeapon(gameState.getSideTurn()) != null
                && gameState.getActiveWeapon(gameState.getSideTurn()).isHasAttack()) {
            gameState.resetSelected(side);
            gameState.setHeroSelected(side, true);
        }
    }

    @Override
    public abstract void selectHeroPower(Side client, Side side);

    protected void selectHeroPower(Side side) {
        if (side != gameState.getSideTurn()) return;
        getActionHolderMap().get(ActionType.DO_ACTION).doAction(gameState.getHeroPower(side)
                , null, this);
    }

    @Override
    public abstract void selectMinion(Side client, Side side, int index, int emptyIndex);

    protected void selectMinion(Side side, int index, int emptyIndex) {
        if (side == gameState.getSideTurn() && gameState.getSelectedMinionOnHand(side) != null) {
            if (emptyIndex > gameState.getGround(side).size())
                emptyIndex = gameState.getGround(side).size();
            gameState.getSelectedMinionOnHand(side).play(this, emptyIndex);
            gameState.setSelectedMinionOnHand(side, null);
            return;
        }
        if (index >= gameState.getGround(side).size()) {
            return;
        }
        MinionLogic selected = gameState.getGround(side).get(index);
        if (gameState.getWaitForTarget(gameState.getSideTurn()) != null) {
            actionHolderMap.get(ActionType.Do_ACTION_FOR_TARGET)
                    .doAction(gameState.getWaitForTarget(gameState.getSideTurn())
                            , selected, this);
            return;
        }
        if (gameState.isHeroPowerSelected(gameState.getSideTurn())) {
            getActionHolderMap().get(ActionType.DO_ACTION).doAction(
                    gameState.getHeroPower(gameState.getSideTurn()), selected, this);
            return;
        }
        if (side != gameState.getSideTurn()) {
            if (gameState.getTaunts(side) == 0 || selected.isHasTaunt()) {
                if (gameState.getSelectedMinionOnGround(gameState.getSideTurn()) != null) {
                    attackMinionToMinion(gameState.getSelectedMinionOnGround(gameState.getSideTurn()), selected);
                    gameState.resetSelected(side.getOther());
                } else if (gameState.isHeroSelected(side.getOther())) {
                    attackHeroToMinion(side.getOther(), selected);
                    gameState.resetSelected(side.getOther());
                }
            }
        } else {
            if (selected.canAttackToMinion()) {
                gameState.resetSelected(side);
                gameState.setSelectedMinionOnGround(gameState.getSideTurn(), selected);
            }
        }
    }

    @Override
    public abstract void selectCardInHand(Side client, Side side, int index);

    public void selectCardInHand(Side side, int index) {
        if (side != gameState.getSideTurn())
            return;
        gameState.resetSelected(side);
        List<CardLogic> hand = gameState.getHand(side);
        if (index >= hand.size()) {
            return;
        }
        hand.get(index).play(this);
    }

    @Override
    public abstract void nextTurn(Side client);

    protected void nextTurn() {
        timer.cancelTask();
        visitAll(this, ActionType.END_TURN, null, gameState.getSideTurn());
        drawCard(gameState.getSideTurn().getOther());
        GameEvent gameEvent = new EndTurn(gameState.getSideTurn());
        gameState.getGameEvents().add(gameEvent);
        if (gameState.getSideTurn() == Side.PLAYER_TWO) gameState.setTurnNumber(gameState.getTurnNumber() + 1);
        int mana = Math.min(gameState.getTurnNumber(), Constants.MAX_MANA);
        if (gameState.getActiveWeapon(gameState.getSideTurn()) != null)
            gameState.getActiveWeapon(gameState.getSideTurn()).setHasAttack(false, gameState);
        gameState.setSideTurn(gameState.getSideTurn().getOther());
        gameState.setMana(gameState.getSideTurn(), mana);
        if (gameState.getActiveWeapon(gameState.getSideTurn()) != null)
            gameState.getActiveWeapon(gameState.getSideTurn()).setHasAttack(true, gameState);
        gameState.getGround(gameState.getSideTurn()).forEach(
                minionLogic -> minionLogic.removeRushAndGiveSleep(gameState));
        visitAll(this, ActionType.START_TURN, null, gameState.getSideTurn());
        turnStartTime = System.currentTimeMillis();
        timer.setTask(this::nextTurn, Constants.TURN_TIME);
    }

    @Override
    public abstract void endGame(Side client);

    public void attackMinionToHero(MinionLogic minionLogic, Side heroSide) {
        int indexOnGround = gameState.getGround(heroSide.getOther()).indexOf(minionLogic);
        HeroLogic heroLogic = gameState.getHero(heroSide);
        heroLogic.dealDamage(minionLogic.getAttack(), this, true);
        minionLogic.use();
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ATTACK_MINION_TO_HERO)
                .setIndex(indexOnGround).setOverview(minionLogic.getMinionOverview())
                .setSide(minionLogic.getSide().getIndex()).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new Attack(minionLogic.getSide(), minionLogic.getCard(), heroLogic.getHero());
        gameState.getGameEvents().add(gameEvent);
    }

    public void attackHeroToHero(Side attackerSide) {
        HeroLogic defender = gameState.getHero(attackerSide.getOther());
        WeaponLogic attacker = gameState.getActiveWeapon(attackerSide);
        defender.dealDamage(attacker.getAttack(), this, true);
        attacker.use(gameState);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ATTACK_HERO_TO_HERO)
                .setOverview(attacker.getOverview())
                .setSide(attackerSide.getIndex()).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new Attack(attackerSide, attacker.getCard(), defender.getHero());
        gameState.getGameEvents().add(gameEvent);
    }

    public void attackMinionToMinion(MinionLogic attacker, MinionLogic defender) {
        int indexOfDefender = gameState.getGround(defender.getSide()).indexOf(defender);
        int indexOfAttacker = gameState.getGround(attacker.getSide()).indexOf(attacker);
        attacker.dealDamage(defender.getAttack(), this, false);
        defender.dealDamage(attacker.getAttack(), this, true);
        attacker.use();
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ATTACK_MINION_TO_MINION)
                .setOverview(attacker.getMinionOverview()).setSide(attacker.getSide().getIndex())
                .setIndex(indexOfAttacker)
                .setSecondIndex(indexOfDefender).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new Attack(attacker.getSide(), attacker.getCard(), defender.getCard());
        gameState.getGameEvents().add(gameEvent);
    }

    public void attackHeroToMinion(Side attackerSide, MinionLogic defender) {
        int indexOnGround = gameState.getGround(attackerSide.getOther()).indexOf(defender);
        WeaponLogic attacker = gameState.getActiveWeapon(attackerSide);
        HeroLogic heroLogic = gameState.getHero(attackerSide);
        heroLogic.dealDamage(defender.getAttack(), this, false);
        defender.dealDamage(attacker.getAttack(), this, true);
        attacker.use(gameState);
        PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ATTACK_HERO_TO_MINION)
                .setOverview(attacker.getOverview()).setOverview1(heroLogic.getHeroOverview())
                .setSide(attackerSide.getIndex()).setIndex(indexOnGround).build();
        gameState.getEvents().add(event);
        GameEvent gameEvent = new Attack(attackerSide, attacker.getCard(), defender.getCard());
        gameState.getGameEvents().add(gameEvent);
    }

    public void startGame() {
        init(Side.PLAYER_ONE);
        init(Side.PLAYER_TWO);
        gameState.setTurnNumber(1);
        gameState.setMana(gameState.getSideTurn(), 1);
        visitAll(this, ActionType.START_TURN, null, gameState.getSideTurn());
        turnStartTime = System.currentTimeMillis();
        timer.setTask(this::nextTurn, Constants.TURN_TIME);
    }

    private void init(Side side) {
        for (CardLogic cardLogic : gameState.getHand(side)) {
            visitAll(this, ActionType.DRAW_CARD, cardLogic, side);
        }
    }

    protected abstract String getEventLog(Side client);

    protected List<PlayDetails.Event> getEvents(Side client) {
        List<PlayDetails.Event> result = new ArrayList<>();
        for (int i = gameState.getEventIndex(client); i < gameState.getEvents().size(); i++) {
            PlayDetails.Event observed = observe(client, gameState.getEvents().get(i));
            if (observed != null)
                result.add(observed);
        }
        gameState.setEventIndex(client, gameState.getEvents().size());
        return result;
    }

    protected abstract PlayDetails.Event observe(Side client, PlayDetails.Event event);

    public void drawCard(Side side) {
        if (gameState.getDeck(side).size() > 0) {
            int randomIndex = (int) (Math.random() * gameState.getDeck(side).size());
            CardLogic randomCard = gameState.getDeck(side).remove(randomIndex);
            drawCard(side, randomCard);
        } else {
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.END_GAME)
                    .setSide(side.getIndex()).build();
            getGameState().getEvents().add(event);
        }
    }

    public void drawCard(Side side, CardLogic cardLogic) {
        if (gameState.getHand(side).size() < Constants.MAX_HAND_SIZE) {
            gameState.getHand(side).add(0, cardLogic);
            visitAll(this, ActionType.DRAW_CARD, cardLogic, side);
            PlayDetails.Event event = new PlayDetails.EventBuilder(ADD_TO_HAND)
                    .setOverview(new CardOverview(cardLogic.getCard())).setSide(side.getIndex()).build();
            GameEvent gameEvent = new DrawCard(side, cardLogic.getCard());
            gameState.getEvents().add(event);
            gameState.getGameEvents().add(gameEvent);
        } else {
            PlayDetails.Event event = new PlayDetails.EventBuilder(SHOW_MESSAGE)
                    .setSide(side.getIndex()).setMessage("your Hand is full!!!!!").build();
            GameEvent gameEvent = new DeleteCard(side, cardLogic.getCard());
            gameState.getEvents().add(event);
            gameState.getGameEvents().add(gameEvent);
        }
    }

    public void playMinion(MinionLogic minionLogic) {
        Side side = minionLogic.getSide();
        Minion minion = minionLogic.getMinion();
        if (gameState.getGround(side).size() < Constants.MAX_GROUND_SIZE) {
            if (minion.getMana() <= gameState.getMana(side)) {
                gameState.setSelectedMinionOnHand(side, minionLogic);
            }
        } else {
            PlayDetails.Event event = new PlayDetails.EventBuilder(SHOW_MESSAGE)
                    .setSide(side.getIndex()).setMessage("ground is full!!!!").build();
            gameState.getEvents().add(event);
        }
    }

    @Override
    public Response getResponse(Side client) {
        if (!isRunning() && gameState.getEventIndex(client) == gameState.getEvents().size())
            return null;
        double time = (System.currentTimeMillis() - turnStartTime) / 60000d;
        PlayDetails response = new PlayDetails(getEventLog(client), gameState.getManas(client), time);
        response.getEvents().addAll(getEvents(client));
        return response;
    }
}