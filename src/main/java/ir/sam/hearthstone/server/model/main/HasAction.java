package ir.sam.hearthstone.server.model.main;

import java.util.Map;

public interface HasAction {
    String getClassName();

    String getName();

    Map<ActionType, String> getMethods();
}
