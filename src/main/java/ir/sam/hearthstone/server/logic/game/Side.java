package ir.sam.hearthstone.server.logic.game;

public enum Side {
    PLAYER_ONE {
        @Override
        public Side getOther() {
            return PLAYER_TWO;
        }
    }, PLAYER_TWO {
        @Override
        public Side getOther() {
            return PLAYER_ONE;
        }
    };

    public abstract Side getOther();
}
