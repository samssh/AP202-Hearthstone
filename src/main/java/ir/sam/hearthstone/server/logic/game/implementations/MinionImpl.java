package ir.sam.hearthstone.server.logic.game.implementations;

import ir.sam.hearthstone.server.model.main.ClassOfCard;
import ir.sam.hearthstone.server.model.main.Minion;
import ir.sam.hearthstone.server.model.main.Rarity;
import ir.sam.hearthstone.response.PlayDetails;
import ir.sam.hearthstone.server.Server;
import ir.sam.hearthstone.server.logic.game.AbstractGame;
import ir.sam.hearthstone.server.logic.game.GameState;
import ir.sam.hearthstone.server.logic.game.Side;
import ir.sam.hearthstone.server.logic.game.behavioral_models.*;
import ir.sam.hearthstone.client.view.model.CardOverview;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class MinionImpl {
    private MinionImpl() {
    }

    private static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }

    private static <T> Optional<T> pickRandom(List<T> ts) {
        int randomIndex = (int) (ts.size() * Math.random());
        return ts.size() > 0 ? Optional.of(ts.get(randomIndex)) : Optional.empty();
    }

    /**
     * BATTLE_CRY
     */
    private static void arenaFanatic(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        List<CardLogic> hand = game.getGameState().getHand(game.getGameState().getSideTurn());
        hand.stream().filter(cardLogic -> cardLogic instanceof MinionLogic)
                .filter(cardLogic -> complexLogic != cardLogic)
                .map(cardLogic -> ((MinionLogic) cardLogic)).forEach(minionLogic ->
                minionLogic.gain(1, 1, game.getGameState(), true, true));
    }

    /**
     * OVERKILL
     */
    private static void arenaPatron(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic instanceof MinionLogic) {
            MinionLogic dead = ((MinionLogic) characterLogic);
            MinionLogic neW = new MinionLogic(dead.getSide(), dead.getMinion());
            GameState gameState = game.getGameState();
            Side side = dead.getSide();
            if (gameState.getGround(side).size() < Server.MAX_GROUND_SIZE) {
                neW.summon(game, gameState.getGround(side).size());
            }
        }
    }

    /**
     * END_TURN
     */
    private static void baronGeddon(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        List<LiveCharacter> liveCharacters = new LinkedList<>();
        liveCharacters.addAll(gameState.getGround(side));
        liveCharacters.addAll(gameState.getGround(side.getOther()));
        liveCharacters.add(gameState.getHero(side));
        liveCharacters.add(gameState.getHero(side.getOther()));
        liveCharacters.stream().filter(liveCharacter -> liveCharacter != complexLogic)
                .forEach(liveCharacter -> liveCharacter.dealDamage(2, game, true));
    }

    /**
     * BATTLE_CRY
     */
    private static void bigGameHunter(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        List<MinionLogic> hand = game.getGameState().getGround(game.getGameState().getSideTurn().getOther());
        pickRandom(hand.stream().filter(minionLogic -> minionLogic.getAttack() >= 7).collect(Collectors.toList()))
                .ifPresent(minionLogic -> minionLogic.kill(game, true));
    }

    /**
     * BATTLE_CRY
     */
    private static void crazed(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        List<MinionLogic> ground = game.getGameState().getGround(game.getGameState().getSideTurn());
        pickRandom(ground.stream().filter(minionLogic -> minionLogic != characterLogic)
                .collect(Collectors.toList()))
                .ifPresent(minionLogic -> minionLogic.gain(4, 0, game.getGameState()
                        , true, false));
    }

    /**
     * DRAW_CARD
     */
    private static void curio(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (complexLogic instanceof MinionLogic) {
            ((MinionLogic) complexLogic).gain(1, 1, game.getGameState(), true, false);
        }
    }

    /**
     * END_TURN
     */
    private static void dreadscale(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        List<LiveCharacter> liveCharacters = new LinkedList<>();
        liveCharacters.addAll(gameState.getGround(side));
        liveCharacters.addAll(gameState.getGround(side.getOther()));
        liveCharacters.stream().filter(liveCharacter -> liveCharacter != complexLogic)
                .forEach(liveCharacter -> liveCharacter.dealDamage(1, game, true));
    }

    /**
     * SUMMON_MINION
     */
    private static void highPriest(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic instanceof MinionLogic && complexLogic instanceof MinionLogic) {
            MinionLogic summoned = ((MinionLogic) characterLogic);
            MinionLogic highPriest = ((MinionLogic) complexLogic);
            summoned.setHp(highPriest.getHp());
        }
    }

    /**
     * BATTLE_CRY
     */
    private static void khartutB(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (complexLogic instanceof MinionLogic) {
            ((MinionLogic) complexLogic).giveTaunt(game);
        }
    }

    /**
     * DEATH_RATTLE
     */
    private static void khartutD(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        gameState.getHero(complexLogic.getSide()).restore(3, gameState);
    }

    /**
     * DEATH_RATTLE
     */
    private static void kobold(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        gameState.getHero(complexLogic.getSide().getOther()).dealDamage(3, game, true);
    }

    /**
     * SPELL_DAMAGE
     */
    private static void malygos(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic instanceof SpellLogic) {
            ((SpellLogic) characterLogic).setValue(((SpellLogic) characterLogic).getValue() + 5);
        }
    }

    /**
     * BATTLE_CRY
     */
    private static void potion(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        List<LiveCharacter> liveCharacters = new ArrayList<>(gameState.getGround(side));
        liveCharacters.add(gameState.getHero(side));
        liveCharacters.stream().filter(liveCharacter -> liveCharacter != complexLogic)
                .forEach(liveCharacter -> liveCharacter.restore(2, game.getGameState()));
    }

    /**
     * PLAY_CARD:
     * PLAY_WEAPON,
     * PLAY_MINION,
     * PLAY_SPELL,
     */
    private static void questing(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (complexLogic instanceof MinionLogic) {
            ((MinionLogic) complexLogic).gain(1, 1, game.getGameState(), true, false);
        }
    }

    /**
     * BATTLE_CRY
     */
    private static void ratcatcher(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (complexLogic instanceof MinionLogic) {
            MinionLogic minionLogic = (MinionLogic) complexLogic;
            GameState gameState = game.getGameState();
            AtomicInteger hp = new AtomicInteger(0), attack = new AtomicInteger(0);
            pickRandom(gameState.getGround(minionLogic.getSide())).ifPresent(dead -> {
                hp.set(dead.getHp());
                attack.set(dead.getAttack());
                dead.kill(game, true);
            });
            minionLogic.gain(attack.get(), hp.get(), gameState, false, false);
        }
    }

    /**
     * BATTLE_CRY
     */
    private static void sathrovarrB(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), complexLogic);
    }

    /**
     * Do_ACTION_FOR_TARGET
     */
    private static void sathrovarrT(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (complexLogic == characterLogic) return;
        if (characterLogic instanceof MinionLogic) {
            GameState gameState = game.getGameState();
            Side side = gameState.getSideTurn();
            MinionLogic main = ((MinionLogic) characterLogic);
            MinionLogic forGround = new MinionLogic(side, main.getMinion());
            MinionLogic forHand = new MinionLogic(side, main.getMinion());
            MinionLogic forDeck = new MinionLogic(side, main.getMinion());
            forGround.summon(game, gameState.getGround(side).size());
            gameState.getDeck(side).add(forDeck);
            gameState.getHand(side).add(forHand);
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ADD_TO_HAND)
                    .setSide(side.getIndex()).setOverview(new CardOverview(forHand.getMinion()))
                    .setIndex(gameState.getHand(side).size()).build();
            gameState.getEvents().add(event);
            game.getGameState().setWaitForTarget(game.getGameState().getSideTurn(), null);
        }
    }

    private final static ClassOfCard neutral = new ClassOfCard("Neutral");
    private final static ClassOfCard hunter = new ClassOfCard("Hunter");
    private final static ClassOfCard warlock = new ClassOfCard("Warlock");

    /**
     * MINION_TAKE_DAMAGE
     */
    private static void security(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        Minion wisp = new Minion("Wisp", "", 6, neutral
                , Rarity.Common, 0, 2, 3);
        MinionLogic mech = new MinionLogic(complexLogic.getSide(), wisp);
        mech.giveTaunt(game);
        mech.summon(game, game.getGameState().getGround(complexLogic.getSide()).size());
    }

    /**
     * ENEMY_PLAY_MINION
     */
    private static void kingDred(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (characterLogic instanceof MinionLogic && complexLogic instanceof MinionLogic && ((MinionLogic) characterLogic).getHp() > 0)
            game.attackMinionToMinion(((MinionLogic) complexLogic), ((MinionLogic) characterLogic));
    }


    private final static List<Minion> legendaryList = Arrays.asList(new Minion("Dreadscale"
                    , "At the end of your turn, deal 1 damage to all other minions.",
                    7, warlock, Rarity.Legendary, 3, 4, 2)
            , new Minion("Malygos", "Spell Damage +5.",
                    7, neutral, Rarity.Legendary, 6, 4, 12)
            , new Minion("Baron Geddon"
                    , "At the end of your turn, deal 2 damage to all other characters.",
                    6, neutral, Rarity.Legendary, 7, 7, 5)
            , new Minion("Sathrovarr",
                    "Choose a friendly minion. Add a copy of it to your hand, deck and battlefield.",
                    7, neutral, Rarity.Legendary, 9, 5, 5)
            , new Minion("High Priest Amet", "Whenever you summon a minion, set its Health equal to this minion's.",
                    6, neutral, Rarity.Legendary, 4, 2, 7)
            , new Minion("Swamp King Dred", "After your opponent plays a minion, attack it.",
                    7, hunter, Rarity.Legendary, 7, 9, 9));

    /**
     * BATTLE_CRY
     */
    private static void tomb(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        if (complexLogic instanceof MinionLogic) {
            MinionLogic clone = new MinionLogic(complexLogic.getSide(), ((MinionLogic) complexLogic).getMinion());
            ((MinionLogic) complexLogic).giveTaunt(game);
            clone.giveTaunt(game);
            clone.summon(game, game.getGameState().getGround(complexLogic.getSide()).size());
        }
    }

    /**
     * DEATH_RATTLE
     */
    private static void pinata(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        pickRandom(legendaryList).ifPresent(minion -> {
            Side side = characterLogic.getSide();
            MinionLogic minionLogic = new MinionLogic(side, minion);
            game.getGameState().getHand(side).add(minionLogic);
            PlayDetails.Event event = new PlayDetails.EventBuilder(PlayDetails.EventType.ADD_TO_HAND)
                    .setSide(side.getIndex()).setIndex(game.getGameState().getHand(side).size())
                    .setOverview(new CardOverview(minion)).build();
            game.getGameState().getEvents().add(event);
        });
    }

    /**
     * END_TURN
     */
    private static void young(ComplexLogic complexLogic, CharacterLogic characterLogic, AbstractGame game) {
        GameState gameState = game.getGameState();
        Side side = gameState.getSideTurn();
        pickRandom(gameState.getGround(side).stream().filter(minionLogic -> minionLogic != complexLogic)
                .collect(Collectors.toList())).ifPresent(minionLogic ->
                minionLogic.gain(0, 1, gameState, true, false));
    }
}