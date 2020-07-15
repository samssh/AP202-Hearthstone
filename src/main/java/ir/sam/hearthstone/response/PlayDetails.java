package ir.sam.hearthstone.response;

import ir.sam.hearthstone.client.Client;
import ir.sam.hearthstone.view.model.UnitOverview;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PlayDetails extends Response{
    @Getter
    private final List<Event> events;
    @Getter
    private final String eventLog;
    @Getter
    private final int[] mana;

    public PlayDetails(String eventLog, int[] mana) {
        this.eventLog = eventLog;
        this.mana = mana;
        events = new ArrayList<>();
    }

    @Override
    public void execute(Client client) {
        client.setPlayDetail(events,eventLog,mana);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setPlayDetailsInfo(this);
    }


    public static class Event {
        @Getter
        private final EventType type;
        @Getter
        private final UnitOverview overview;
        @Getter
        private final int index;
        @Getter
        private final int side;

        public Event(EventType type, UnitOverview overview, int index, int side) {
            this.type = type;
            this.overview = overview;
            this.index = index;
            this.side = side;
        }

        public Event(EventType type, UnitOverview overview,int side) {
            this(type, overview, -1, side);
        }
    }

    public enum EventType {
        SET_HERO,
        SET_HERO_POWER,
        ADD_TO_GROUND,
        ADD_TO_HAND,
        MOVE_FROM_HAND_TO_GROUND,
        MOVE_FROM_GROUND_TO_HAND,
        ATTACK_MINION_TO_HERO,
        ATTACK_MINION_TO_MINION,
        ATTACK_HERO_TO_MINION,
        ATTACK_HERO_TO_HERO,
        PLAY_SPELL,
        PLAY_WEAPON,


    }
}