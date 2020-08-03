package ir.sam.hearthstone.server.controller.logic.game;

public enum Side {
    PLAYER_ONE {
        @Override
        public Side getOther() {
            return PLAYER_TWO;
        }

        @Override
        public int getIndex() {
            return 0;
        }
    }, PLAYER_TWO {
        @Override
        public Side getOther() {
            return PLAYER_ONE;
        }

        @Override
        public int getIndex() {
            return 1;
        }
    };

    public abstract Side getOther();

    public abstract int getIndex();
}
