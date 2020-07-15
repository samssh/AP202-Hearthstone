package ir.sam.hearthstone.model.main;

import java.util.Map;

public interface HasAction {
    String getClassName();

    String getName();

    Map<ActionType, String> getMethods();
}
