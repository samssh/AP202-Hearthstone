package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.view.model.UnitOverview;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
    private final long time;

    public PlayDetails(String eventLog, int[] mana, long time) {
        this.eventLog = eventLog;
        this.mana = mana;
        this.time = time;
        events = new ArrayList<>();
    }

    @Override
    public void execute(Client client) {
        client.setPlayDetail(events, eventLog, mana, time);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setPlayDetailsInfo(this);
    }

    @ToString
    public static class Event {
        @Getter
        private final EventType type;
        @Getter
        private final UnitOverview overview, overview1;
        @Getter
        private final int index, secondIndex;
        @Getter
        private final int side;
        @Getter
        private final String message;

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
    }

    public static class EventBuilder{
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

        public Event build(){
            return new Event(type,overview,overview1,index,side,secondIndex,message);
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