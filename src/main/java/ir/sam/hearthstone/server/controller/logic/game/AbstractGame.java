package ir.sam.hearthstone.server.controller.logic.game;

import ir.sam.hearthstone.server.controller.Constants;
import ir.sam.hearthstone.server.controller.logic.game.api.Game;
import ir.sam.hearthstone.server.controller.logic.game.behavioral_models.*;
import ir.sam.hearthstone.server.controller.logic.game.events.Attack;
import ir.sam.hearthstone.server.controller.logic.game.events.DeleteCard;
import ir.sam.hearthstone.server.controller.logic.game.events.DrawCard;
import ir.sam.hearthstone.server.controller.logic.game.events.GameEvent;
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
    protected final Map<ActionType, ActionHolder> actionHolderMap;
    @Getter
    protected long turnStartTime;
    @Getter
    protected final TaskTimer timer;

    public AbstractGame(GameState gameState, ModelLoader modelLoader) {
        this.gameState = gameState;
        actionHolderMap = ActionHolderBuilder.getAllActionHolders(modelLoader);
        timer = new TaskTimer();
    }

    @Override
    public abstract void selectHero(Side client, Side side);

    @Override
    public abstract void selectHeroPower(Side client, Side side);

    @Override
    public abstract void selectMinion(Side client, Side side, int index, int emptyIndex);

    @Override
    public abstract void selectCardInHand(Side client, Side side, int index);

    @Override
    public abstract void nextTurn(Side client);

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
        gameState.setSideTurn(gameState.getSideTurn().getOther());
        int mana = Math.min(gameState.getTurnNumber(), Constants.MAX_MANA);
        gameState.setMana(gameState.getSideTurn(), mana);
        visitAll(this, ActionType.START_TURN, null, gameState.getSideTurn());
        turnStartTime = System.currentTimeMillis();
        timer.setTask(() -> nextTurn(Side.PLAYER_ONE), Constants.TURN_TIME);
    }

    private void init(Side side) {
        for (CardLogic cardLogic : gameState.getHand(side)) {
            visitAll(this, ActionType.DRAW_CARD, cardLogic, side);
        }
    }

    public abstract String getEventLog(Side client);

    public abstract List<PlayDetails.Event> getEvents(Side client);

    public void drawCard(Side side) {
        if (gameState.getDeck(side).size() > 0) {
            int randomIndex = (int) (Math.random() * gameState.getDeck(side).size());
            CardLogic randomCard = gameState.getDeck(side).remove(randomIndex);
            drawCard(side, randomCard);
        } else {
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.END_GAME)
                    .setMessage(side + " lose").build();
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
                    .setMessage("your Hand is full!!!!!").build();
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
                    .setMessage("ground is full!!!!").build();
            gameState.getEvents().add(event);
        }
    }

    @Override
    public Response getResponse(Side client) {
        PlayDetails response = new PlayDetails(getEventLog(client), gameState.getMana(), turnStartTime);
        response.getEvents().addAll(getEvents(client));
        return response;
    }
}