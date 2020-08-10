package ir.sam.hearthstone.server.model.response;

import ir.sam.hearthstone.server.model.client.UnitOverview;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

public class PlayDetails extends Response {
    @Getter
    private final List<Event> events;
    @Getter
    private final String eventLog;
    @Getter
    private final int[] mana;
    @Getter
    private final double time;

    public PlayDetails(String eventLog, int[] mana, double time) {
        this.eventLog = eventLog;
        this.mana = mana;
        this.time = time;
        events = new ArrayList<>();
    }

    @Override
    public void execute(ResponseExecutor responseExecutor) {
        responseExecutor.setPlayDetail(events, eventLog, mana, time);
    }

    public static class Event implements Cloneable {
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

        private Event(EventType type, UnitOverview overview, UnitOverview overview1
                , int index, int side, int secondIndex, String message) {
            this.type = type;
            this.overview = overview;
            this.overview1 = overview1;
            this.index = index;
            this.secondIndex = secondIndex;
            this.side = side;
            this.message = message;
        }

        @Override
        public Event clone() {
            try {
                return (Event) super.clone();
            } catch (CloneNotSupportedException e) {
                // this shouldn't happen, since we are Cloneable
            }
            return null;
        }

        @Override
        public String toString() {
            return "Event{" +
                    "type=" + type +
                    ", overview=" + overview +
                    ", overview1=" + overview1 +
                    ", index=" + index +
                    ", secondIndex=" + secondIndex +
                    ", side=" + side +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    public static class EventBuilder {
        private final EventType type;
        @Setter
        @Accessors(chain = true)
        private UnitOverview overview, overview1;
        @Setter
        @Accessors(chain = true)
        private int index, secondIndex;
        @Setter
        @Accessors(chain = true)
        private int side;
        @Setter
        @Accessors(chain = true)
        private String message;

        public EventBuilder(EventType type) {
            this.type = type;
            index = -1;
            secondIndex = -1;
            side = -1;
        }

        public Event build() {
            return new Event(type, overview, overview1, index, side, secondIndex, message);
        }
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
        @SuppressWarnings("unused")
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