package ir.sam.hearthstone.client.model.response;

import ir.sam.hearthstone.client.model.main.UnitOverview;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class PlayDetails extends Response {
    @Getter
    @Setter
    private List<Event> events;
    @Getter
    @Setter
    private String eventLog;
    @Getter
    @Setter
    private int[] mana;
    @Getter
    @Setter
    private long time;

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setPlayDetail(events, eventLog, mana, time);
    }

    @ToString(includeFieldNames = false)
    public static class Event {
        @Getter
        @Setter
        private EventType type;
        @Getter
        @Setter
        private UnitOverview overview, overview1;
        @Getter
        @Setter
        private int index, secondIndex;
        @Getter
        @Setter
        private int side;
        @Getter
        @Setter
        private String message;
    }

    public enum EventType {
        SET_HERO,
        SET_HERO_POWER,
        ADD_TO_HAND,
        CHANGE_IN_HAND,
        REMOVE_FROM_HAND,
        MOVE_FROM_HAND_TO_GROUND,
        ADD_TO_GROUND,
        CHANGE_IN_GROUND,
        REMOVE_FROM_GROUND,
        MOVE_FROM_GROUND_TO_HAND,
        ATTACK_MINION_TO_HERO,
        ATTACK_MINION_TO_MINION,
        ATTACK_HERO_TO_MINION,
        ATTACK_HERO_TO_HERO,
        ATTACK_HERO_POWER_TO_HERO,
        ATTACK_HERO_POWER_TO_MINION,
        PLAY_SPELL,
        PLAY_WEAPON,
        CHANGE_WEAPON,
        SHOW_MESSAGE,
        END_GAME
    }
}